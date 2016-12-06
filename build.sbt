import sbt.URL

sbtPlugin := true


// Basic application information.
// ---------------------------------------
organization := "com.github.gpgekko"
organizationName := "gpgekko"
organizationHomepage := Some(new URL("https://github.com/gpgekko"))
name := "sbt-autoprefixer"
homepage := Some(new URL("https://github.com/gpgekko/sbt-autoprefixer"))
startYear := Some(2016)
version := "1.1.1"


// Scala version to use.
// ---------------------------------------
scalaVersion := "2.10.6"


// Library dependencies.
// ---------------------------------------
libraryDependencies ++= Seq(
   "org.webjars.npm" % "autoprefixer" % "6.4.0",
   "org.webjars.npm" % "postcss-cli" % "2.6.0",
   "org.webjars.npm" % "semver" % "5.3.0"
)

// Tell SBT to use the override the default resolver settings.
// ---------------------------------------
overrideBuildResolvers := true


// Plugins.
// ---------------------------------------
addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.1.4")