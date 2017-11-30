import com.typesafe.sbt.web.SbtWeb
import com.typesafe.sbt.web.SbtWeb.autoImport.WebJs._
import com.typesafe.sbt.web.SbtWeb.autoImport._
import JsEngineKeys._
import com.github.gpgekko.sbt.autoprefixer._
import sbt.Keys._
import sbt._

lazy val root = (project in file(".")).enablePlugins(SbtWeb)

engineType in autoprefixer := EngineType.Node

pipelineStages := Seq(autoprefixer)

autoPrefixerBrowsers in autoprefixer := com.typesafe.sbt.web.js.JS.Array("safari 5")

val checkCSSFileContents = taskKey[Unit]("check that css contents are correct")

checkCSSFileContents := {
  val contents = IO.read(file("target/web/stage/css/test.css"))
  if (!contents.contains(":-webkit-full-screen")) {
    sys.error(s"Unexpected contents: $contents")
  }
}