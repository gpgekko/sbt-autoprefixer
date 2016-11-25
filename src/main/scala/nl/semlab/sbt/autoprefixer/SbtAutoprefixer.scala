package nl.semlab.sbt.autoprefixer

import com.typesafe.sbt.jse.SbtJsEngine
import com.typesafe.sbt.jse.SbtJsTask
import com.typesafe.sbt.web.PathMapping
import com.typesafe.sbt.web.SbtWeb
import com.typesafe.sbt.web.incremental
import com.typesafe.sbt.web.incremental.OpFailure
import com.typesafe.sbt.web.incremental.OpInputHash
import com.typesafe.sbt.web.incremental.OpInputHasher
import com.typesafe.sbt.web.incremental.OpSuccess
import com.typesafe.sbt.web.pipeline.Pipeline
import sbt.Keys._
import sbt._

object Import {

   val autoprefixer = TaskKey[Pipeline.Stage]("autoprefixer", "Autoprefixes CSS files.")

   object AutoprefixerKeys {
      val buildDir = SettingKey[File]("uglify-build-dir", "Where autoprefixer will write to.")
      val dir = SettingKey[File]("uglify-input-dir", "Where autoprefixer will read from. It likes to have all the files in one place.")
   }
}

object SbtAutoprefixer extends AutoPlugin {

   override def requires = SbtJsTask

   override def trigger = AllRequirements

   val autoImport = Import

   import SbtJsEngine.autoImport.JsEngineKeys._
   import SbtJsTask.autoImport.JsTaskKeys._
   import SbtWeb.autoImport._
   import WebKeys._
   import autoImport._
   import AutoprefixerKeys._

   override def projectSettings = Seq(
      autoprefixer := autoPrefixFiles.dependsOn(webJarsNodeModules in Plugin).value,
      buildDir := (resourceManaged in autoprefixer).value / "build",
      dir := (resourceManaged in autoprefixer).value / "app",
      excludeFilter in autoprefixer := new SimpleFileFilter(file => file.relativeTo(baseDirectory.value).get.getPath contains "lib") || HiddenFileFilter,
      includeFilter in autoprefixer := "*.css",
      resourceManaged in autoprefixer := webTarget.value / autoprefixer.key.label
   )

   private def dotMin(file: String): String = {
      val exti = file.lastIndexOf('.')
      val (pfx, ext) = if (exti == -1) (file, "")
      else file.splitAt(exti)
      pfx + ".min" + ext
   }

   // The meat of the program, does all the heavy lifting
   private def autoPrefixFiles: Def.Initialize[Task[Pipeline.Stage]] = Def.task { mappings =>
      val include = (includeFilter in autoprefixer).value
      val exclude = (excludeFilter in autoprefixer).value
      val optimizerMappings = mappings.filter(f => !f._1.isDirectory && include.accept(f._1) && !exclude.accept(f._1))

      SbtWeb.syncMappings(
         streams.value.cacheDirectory,
         optimizerMappings,
         dir.value
      )

      val appInputMappings = optimizerMappings.map(p => dir.value / p._2 -> p._2)
      val groupings = appInputMappings.map(fp => (Seq(fp), dotMin(fp._2)))

      val options = Seq(
         (excludeFilter in autoprefixer).value,
         (includeFilter in autoprefixer).value,
         (resourceManaged in autoprefixer).value
      ).mkString("|")

      implicit val opInputHasher = OpInputHasher[(Seq[PathMapping], String)](io =>
                           OpInputHash.hashString((io._2 +: io._1.map(_._1.getAbsolutePath)).mkString("|") + "|" + options))

      val (outputFiles, ()) = incremental.syncIncremental(streams.value.cacheDirectory / "run", groupings) { modifiedGroupings: Seq[(Seq[(File, String)], String)] =>
         if (modifiedGroupings.nonEmpty) {
            streams.value.log.info(s"Optimizing ${modifiedGroupings.size} CSS(s) with Autoprefixer")

            val nodeModulePaths = (nodeModuleDirectories in Plugin).value.map(_.getPath)
            val cli = (webJarsNodeModulesDirectory in Plugin).value / "postcss-cli" / "bin" / "postcss"

            val executeAutoprefixer = SbtJsTask.executeJs(
               state.value,
               (engineType in autoprefixer).value,
               (command in autoprefixer).value,
               nodeModulePaths,
               cli,
               _: Seq[String],
               (timeoutPerSource in autoprefixer).value
            )

            val prefixerArgs = Seq("--use", "autoprefixer")

            (modifiedGroupings.map { grouping =>
               val (inputMappings, outputPath) = grouping

               val inputFiles = inputMappings.map(_._1)
               val inputFileArgs = inputFiles.map(_.getPath)

               val outputFile = dir.value / outputPath
               val outputFileArgs = Seq(
                  "--output",
                  outputFile.getPath
               )

               val args = prefixerArgs ++
                           outputFileArgs ++
                           inputFileArgs

               val success = executeAutoprefixer(args).headOption.fold(true)(_ => false)
               grouping -> (
                  if(success) OpSuccess(inputFiles.toSet, Set(outputFile))
                  else OpFailure
               )
            }.toMap, ())

         } else {
            (Map.empty, ())
         }
      }

      (mappings.toSet ++ outputFiles.pair(relativeTo(dir.value))).toSeq
   }
}