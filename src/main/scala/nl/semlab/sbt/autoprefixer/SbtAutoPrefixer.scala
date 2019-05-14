package nl.semlab.sbt.autoprefixer

import java.nio.charset.Charset

import com.typesafe.sbt.jse.SbtJsEngine
import com.typesafe.sbt.jse.SbtJsTask
import com.typesafe.sbt.web.PathMapping
import com.typesafe.sbt.web.SbtWeb
import com.typesafe.sbt.web.incremental
import com.typesafe.sbt.web.incremental._
import com.typesafe.sbt.web.js.JS
import com.typesafe.sbt.web.pipeline.Pipeline
import monix.reactive.Observable
import nl.semlab.sbt.autoprefixer.Compat.SbtIoPath._
import nl.semlab.sbt.autoprefixer.Compat.cacheStore
import sbt.Keys._
import sbt.Task
import sbt._

import scala.concurrent.Await

object Import {
   val autoprefixer        : TaskKey[Pipeline.Stage] = taskKey[Pipeline.Stage]("Autoprefixes CSS files.")

   val autoPrefixerBrowsers: SettingKey[JS.Array]    = settingKey[JS.Array]("Which browsers autoprefixer will support.")
   val autoPrefixerBuildDir: SettingKey[File]        = settingKey[File]("Where autoprefixer will write to.")
   val autoPrefixerConfig  : TaskKey[JS.Object]      = taskKey[JS.Object]("The config contents.")
   val autoPrefixerDir     : SettingKey[File]        = settingKey[File]("Where autoprefixer will read from. It likes to have all the files in one place.")
   val autoPrefixerReplace : SettingKey[Boolean]     = settingKey[Boolean]("If autoprefixer should overwrite the original files.")
}

object SbtAutoPrefixer extends AutoPlugin {

   override def requires: SbtJsTask.type = SbtJsTask

   override def trigger: AllRequirements.type = AllRequirements

   val autoImport: Import.type = Import

   import SbtJsEngine.autoImport.JsEngineKeys._
   import SbtJsTask.autoImport.JsTaskKeys._
   import SbtWeb.autoImport._
   import WebKeys._
   import autoImport._

   override def projectSettings = Seq(
      autoprefixer := autoPrefixFiles.dependsOn(webJarsNodeModules in Plugin).value,
      resourceManaged in autoprefixer := webTarget.value / autoprefixer.key.label,
      autoPrefixerBrowsers := JS.Array("> 1% in NL", "last 2 versions", "Firefox ESR"),
      autoPrefixerBuildDir := (resourceManaged in autoprefixer).value / "build",
      autoPrefixerConfig := getConfig.value,
      deduplicators in autoprefixer += SbtWeb.selectFileFrom((autoPrefixerBuildDir in autoprefixer).value),
      autoPrefixerDir := (resourceManaged in autoprefixer).value / "app",
      excludeFilter in autoprefixer := new SimpleFileFilter(file => file.relativeTo(baseDirectory.value).get.getPath contains "lib") || HiddenFileFilter,
      includeFilter in autoprefixer := "*.css",
      autoPrefixerReplace := true
   )

   private case class PrefixerOpGrouping(inputFiles: Seq[PathMapping], outputFile: String)

   private def dotMin(file: String): String = {
      val exti = file.lastIndexOf('.')
      val (pfx, ext) = if (exti == -1) (file, "") else file.splitAt(exti)
      pfx + ".min" + ext
   }

   private def getConfig: Def.Initialize[Task[JS.Object]] = Def.task {
      JS.Object(
         "autoprefixer" -> JS.Object(
            "browsers" -> autoPrefixerBrowsers.value
         )
      )
   }

   // The meat of the program, does all the heavy lifting
   private def autoPrefixFiles: Def.Initialize[Task[Pipeline.Stage]] = Def.task {
      val resourceManagedValue = (resourceManaged in autoprefixer).value
      val engineTypeValue = (engineType in autoprefixer).value
      val commandValue = (command in autoprefixer).value
      val timeoutPerSourceValue = (timeoutPerSource in autoprefixer).value
      val browsersValue = (autoPrefixerBrowsers in autoprefixer).value
      val configValue = (autoPrefixerConfig in autoprefixer).value
      val excludeFilterValue = (excludeFilter in autoprefixer).value
      val includeFilterValue = (includeFilter in autoprefixer).value
      val replaceValue = (autoPrefixerReplace in autoprefixer).value
      val streamsValue = streams.value
      val inputDirectoryValue = (autoPrefixerDir in autoprefixer).value
      val buildDirectoryValue = (autoPrefixerBuildDir in autoprefixer).value
      val nodeModuleDirectoriesValue = (nodeModuleDirectories in Plugin).value
      val webJarsNodeModulesDirectoryValue = (webJarsNodeModulesDirectory in Plugin).value
      val stateValue = state.value

      val options = Seq(
         browsersValue,
         excludeFilterValue,
         includeFilterValue,
         replaceValue,
         resourceManagedValue
      ).mkString("|")

      mappings => {
         val optimizerMappings = mappings.filter(f => !f._1.isDirectory && includeFilterValue.accept(f._1) && !excludeFilterValue.accept(f._1))

         SbtWeb.syncMappings(
            cacheStore(streamsValue, "autoprefixer"),
            optimizerMappings,
            inputDirectoryValue
         )

         val appInputMappings = optimizerMappings.map(p => inputDirectoryValue / p._2 -> p._2)
         val groupings = appInputMappings.map(fp => {
            if(replaceValue) {
               PrefixerOpGrouping(Seq(fp), fp._2)
            } else {
               PrefixerOpGrouping(Seq(fp), dotMin(fp._2))
            }
         })

         val targetBuildProfileFile = resourceManagedValue / ".postcssrc"
         IO.write(targetBuildProfileFile, configValue.js, Charset.forName("UTF-8"))

         implicit val opInputHasher: OpInputHasher[PrefixerOpGrouping] = OpInputHasher[PrefixerOpGrouping](io =>
            OpInputHash.hashString(
               (io.outputFile +: io.inputFiles.map(_._1.getAbsolutePath)).mkString("|") + "|" + options
            )
         )

         val (outputFiles, ()) = incremental.syncIncremental(streamsValue.cacheDirectory / "run", groupings) { modifiedGroupings: Seq[PrefixerOpGrouping] =>
            if (modifiedGroupings.nonEmpty) {
               streamsValue.log.info(s"Optimizing ${modifiedGroupings.size} CSS file(s) with Autoprefixer")

               val nodeModulePaths = nodeModuleDirectoriesValue.map(_.getPath)
               val cli = webJarsNodeModulesDirectoryValue / "postcss-cli" / "bin" / "postcss"

               def executeAutoPrefixer(args: Seq[String]) = monix.eval.Task {
                  SbtJsTask.executeJs(
                     stateValue.copy(),
                     engineTypeValue,
                     commandValue,
                     nodeModulePaths,
                     cli,
                     args: Seq[String],
                     timeoutPerSourceValue
                  )
               }

               val prefixerArgs = Seq("-c", targetBuildProfileFile.getAbsolutePath, "--use", "autoprefixer")

               val resultObservable: Observable[(PrefixerOpGrouping, OpResult)] = Observable.fromIterable(modifiedGroupings).map { grouping =>
                  val inputFiles = grouping.inputFiles.map(_._1)
                  val inputFileArgs = inputFiles.map(_.getPath)

                  val outputFile = buildDirectoryValue / grouping.outputFile
                  val outputFileArgs = Seq(
                     "--output",
                     outputFile.getPath
                  )

                  val args = prefixerArgs ++
                             outputFileArgs ++
                             inputFileArgs

                  executeAutoPrefixer(args).map { result =>
                     val success = result.headOption.fold(true)(_ => false)
                     grouping -> (
                        if(success) OpSuccess(inputFiles.toSet, Set(outputFile))
                        else OpFailure
                     )
                  }
               }.mergeMap(task => Observable.fromTask(task))

               val prefixerPool = monix.execution.Scheduler.computation(
                  parallelism = java.lang.Runtime.getRuntime.availableProcessors
               )
               val result = Await.result(
                  resultObservable.toListL.runAsync(prefixerPool),
                  timeoutPerSourceValue * modifiedGroupings.size
               )

               (result.toMap, ())
            } else {
               (Map.empty, ())
            }
         }

         SbtWeb.deduplicateMappings((mappings.toSet ++ outputFiles.pair(relativeTo(buildDirectoryValue))).toSeq,
            Seq(SbtWeb.selectFileFrom(buildDirectoryValue)))
      }
   }
}