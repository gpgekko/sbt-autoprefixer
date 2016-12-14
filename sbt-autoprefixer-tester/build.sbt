import WebJs._
import JsEngineKeys._

lazy val root = (project in file(".")).enablePlugins(SbtWeb)

engineType in autoprefixer := EngineType.Node

pipelineStages := Seq(autoprefixer)

browsers := JS.Array("safari 6")
