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


// Plugins.
// ---------------------------------------
addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.1.4")


// Scripted.
// ---------------------------------------
ScriptedPlugin.scriptedSettings
scriptedLaunchOpts := { scriptedLaunchOpts.value ++
                        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}
scriptedBufferLog := false


// Bintray settings.
// ---------------------------------------
publishMavenStyle := false
bintrayOrganization in bintray := None