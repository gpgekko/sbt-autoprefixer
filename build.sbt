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
crossSbtVersions := Seq("0.13.18", "1.2.8")


// Library dependencies.
// ---------------------------------------
libraryDependencies ++= Seq(
   "org.webjars.npm" % "autoprefixer" % "7.1.6" exclude("org.webjars.npm", "browserslist")exclude("org.webjars.npm", "electron-to-chromium"),
   "org.webjars.npm" % "postcss-cli" % "4.1.1" exclude("org.webjars.npm", "validate-npm-package-license")
                                                exclude("org.webjars.npm", "chokidar")
                                                exclude("org.webjars.npm", "graceful-fs")
                                                exclude("org.webjars.npm", "glob")
                                                exclude("org.webjars.npm", "lodash")
                                                exclude("org.webjars.npm", "inherits")
                                                exclude("org.webjars.npm", "hosted-git-info")
                                                exclude("org.webjars.npm", "resolve")
                                                exclude("org.webjars.npm", "js-yaml")
                                                exclude("org.webjars.npm", "atob"),

   "io.monix" %% "monix" % "2.3.3",

   // Some dependencies either fail to resolve or have newer versions that have dependencies that are not available.
   "org.webjars.npm" % "semver" % "5.7.0",
   "org.webjars.npm" % "block-stream" % "0.0.9"exclude("org.webjars.npm", "glob") exclude("org.webjars.npm", "inherits"),
   "org.webjars.npm" % "validate-npm-package-license" % "3.0.1",
   "org.webjars.npm" % "object.omit" % "2.0.1-1",
   "org.webjars.npm" % "micromatch" % "2.3.11",
   "org.webjars.npm" % "graceful-fs" % "4.1.15",
   "org.webjars.npm" % "resolve" % "1.10.1",
   "org.webjars.npm" % "electron-to-chromium" % "1.3.125",
   "org.webjars.npm" % "browserslist" % "2.11.3" exclude("org.webjars.npm", "caniuse-lite"),
   "org.webjars.npm" % "glob" % "7.1.4" exclude("org.webjars.npm", "inherits"),
   "org.webjars.npm" % "lodash" % "4.17.11",
   "org.webjars.npm" % "inherits" % "2.0.3",
   "org.webjars.npm" % "js-yaml" % "3.13.1",
   "org.webjars.npm" % "async-each" % "1.0.3",
   "org.webjars.npm" % "process-nextick-args" % "2.0.0",
   "org.webjars.npm" % "hosted-git-info" % "2.7.1",
   "org.webjars.npm" % "readdirp" % "2.1.0"exclude("org.webjars.npm", "process-nextick-args") exclude("org.webjars.npm", "inherits") exclude("org.webjars.npm", "graceful-fs"),
   "org.webjars.npm" % "anymatch" % "1.3.2" exclude("org.webjars.npm", "micromatch"),
   "org.webjars.npm" % "chokidar" % "1.7.0"exclude("org.webjars.npm", "glob") exclude("org.webjars.npm", "inherits") exclude("org.webjars.npm", "anymatch") exclude("org.webjars.npm", "readdirp")
)

dependencyOverrides += "org.webjars.npm" % "semver" % "5.7.0"
dependencyOverrides += "org.webjars.npm" % "block-stream" % "0.0.9"
dependencyOverrides += "org.webjars.npm" % "validate-npm-package-license" % "3.0.1"
dependencyOverrides += "org.webjars.npm" % "object.omit" % "2.0.1-1"
dependencyOverrides += "org.webjars.npm" % "micromatch" % "2.3.11"
dependencyOverrides += "org.webjars.npm" % "graceful-fs" % "4.1.15"
dependencyOverrides += "org.webjars.npm" % "resolve" % "1.10.1"
dependencyOverrides += "org.webjars.npm" % "electron-to-chromium" % "1.3.125"
dependencyOverrides += "org.webjars.npm" % "browserslist" % "2.11.3" exclude("org.webjars.npm", "caniuse-lite")
dependencyOverrides += "org.webjars.npm" % "glob" % "7.1.4"
dependencyOverrides += "org.webjars.npm" % "lodash" % "4.17.11"
dependencyOverrides += "org.webjars.npm" % "inherits" % "2.0.3"
dependencyOverrides += "org.webjars.npm" % "js-yaml" % "3.13.1"
dependencyOverrides += "org.webjars.npm" % "async-each" % "1.0.3"
dependencyOverrides += "org.webjars.npm" % "process-nextick-args" % "2.0.0"
dependencyOverrides += "org.webjars.npm" % "hosted-git-info" % "2.7.1"
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