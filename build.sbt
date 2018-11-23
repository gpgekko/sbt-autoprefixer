import sbt.URL
import sbt.plugins.SbtPlugin

lazy val root = (project in file("."))
      .enablePlugins(SbtPlugin)


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
crossSbtVersions := Seq("0.13.17", "1.2.6")


// Library dependencies.
// ---------------------------------------
libraryDependencies ++= Seq(
   "org.webjars.npm" % "autoprefixer" % "7.1.6",
   "org.webjars.npm" % "postcss-cli" % "4.1.1" exclude("org.webjars.npm", "validate-npm-package-license")
                                                exclude("org.webjars.npm", "chokidar")
                                                exclude("org.webjars.npm", "atob"),

   // Some dependencies either fail to resolve or have newer versions that have dependencies that are not available.
   "org.webjars.npm" % "semver" % "5.6.0",
   "org.webjars.npm" % "block-stream" % "0.0.9",
   "org.webjars.npm" % "validate-npm-package-license" % "3.0.1",
   "org.webjars.npm" % "object.omit" % "2.0.1-1",
   "org.webjars.npm" % "micromatch" % "2.3.11",
   "org.webjars.npm" % "readdirp" % "2.1.0",
   "org.webjars.npm" % "anymatch" % "1.3.2" exclude("org.webjars.npm", "micromatch"),
   "org.webjars.npm" % "chokidar" % "1.7.0" exclude("org.webjars.npm", "anymatch") exclude("org.webjars.npm", "readdirp")
)

dependencyOverrides += "org.webjars.npm" % "semver" % "5.6.0"
dependencyOverrides += "org.webjars.npm" % "block-stream" % "0.0.9"
dependencyOverrides += "org.webjars.npm" % "validate-npm-package-license" % "3.0.1"
dependencyOverrides += "org.webjars.npm" % "object.omit" % "2.0.1-1"
dependencyOverrides += "org.webjars.npm" % "micromatch" % "2.3.11"
dependencyOverrides += "org.webjars.npm" % "readdirp" % "2.1.0"
dependencyOverrides += "org.webjars.npm" % "anymatch" % "1.3.2"
dependencyOverrides += "org.webjars.npm" % "chokidar" % "1.7.0"


// Plugins.
// ---------------------------------------
addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.2.3")


// Scripted.
// ---------------------------------------
scriptedLaunchOpts ++= Seq("-Xmx1024M", s"-Dplugin.version=${version.value}")
scriptedBufferLog := false


// Bintray settings.
// ---------------------------------------
publishMavenStyle := false
bintrayOrganization in bintray := None