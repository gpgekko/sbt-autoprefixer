import sbt.URL

sbtPlugin := true


// Basic application information.
// ---------------------------------------
organization := "nl.semlab.sbt"
organizationName := "Semlab"
organizationHomepage := Some(new URL("https://www.semlab.nl"))
name := "sbt-autoprefixer"
startYear := Some(2016)


// Scala version to use.
// ---------------------------------------
crossSbtVersions := Seq("0.13.16", "1.0.2")


// Library dependencies.
// ---------------------------------------
libraryDependencies ++= Seq(
   "org.webjars.npm" % "autoprefixer" % "7.1.2",
   "org.webjars.npm" % "postcss-cli" % "4.1.1",

   "org.webjars.npm" % "semver" % "5.4.1",
   "org.webjars.npm" % "block-stream" % "0.0.9"
)

dependencyOverrides += "org.webjars.npm" % "semver" % "5.4.1"
dependencyOverrides += "org.webjars.npm" % "block-stream" % "0.0.9"


// Plugins.
// ---------------------------------------
addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.2.2")


scriptedLaunchOpts ++= Seq("-Xmx1024M", s"-Dplugin.version=${version.value}")
scriptedBufferLog := false


// Nexus location.
// ---------------------------------------
val nexusIp = "192.168.150.190"
val nexusLocation = "http://" + nexusIp + ":8081/nexus/content"


// If we call publish, this is where it should go.
// ---------------------------------------
publishTo := {
   val nexus = nexusLocation + "/repositories/"
   if(isSnapshot.value)
      Some("semlab-snapshots-nexus" at nexus + "snapshots/")
   else
      Some("semlab-releases-nexus" at nexus + "releases/")
}
credentials := Seq(Credentials("Sonatype Nexus Repository Manager", nexusIp, "deployment", "deploy"))


// Building POMs, Maven style.
// ---------------------------------------
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }