import sbt.URL

sbtPlugin := true


// Basic application information.
// ---------------------------------------
name := "sbt-autoprefixer"


// Scala version to use.
// ---------------------------------------
scalaVersion := "2.10.6"


// Library dependencies.
// ---------------------------------------
libraryDependencies ++= Seq(
   "org.webjars.npm" % "autoprefixer" % "6.6.0",
   "org.webjars.npm" % "postcss-cli" % "2.6.0",
   "org.webjars.npm" % "semver" % "5.3.0"
)

// Tell SBT to use the override the default resolver settings.
// ---------------------------------------
overrideBuildResolvers := true


// Plugins.
// ---------------------------------------
addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.1.4")