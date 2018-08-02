lazy val root = (project in file("."))
      .enablePlugins(SbtPlugin)


// Basic application information.
// ---------------------------------------
name := "sbt-autoprefixer"


// Scala version to use.
// ---------------------------------------
crossSbtVersions := Seq("0.13.17", "1.2.0")


// Library dependencies.
// ---------------------------------------
libraryDependencies ++= Seq(
   "org.webjars.npm" % "autoprefixer" % "7.1.6",
   "org.webjars.npm" % "postcss-cli" % "4.1.1" exclude("org.webjars.npm", "validate-npm-package-license"),

   "org.webjars.npm" % "semver" % "5.5.0",
   "org.webjars.npm" % "block-stream" % "0.0.9",
   "org.webjars.npm" % "validate-npm-package-license" % "3.0.1",
   "org.webjars.npm" % "object.omit" % "2.0.1-1"
)

dependencyOverrides += "org.webjars.npm" % "semver" % "5.5.0"
dependencyOverrides += "org.webjars.npm" % "block-stream" % "0.0.9"
dependencyOverrides += "org.webjars.npm" % "validate-npm-package-license" % "3.0.1"
dependencyOverrides += "org.webjars.npm" % "object.omit" % "2.0.1-1"


// Plugins.
// ---------------------------------------
addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.2.2")


scriptedLaunchOpts ++= Seq("-Xmx1024M", s"-Dplugin.version=${version.value}")
scriptedBufferLog := false