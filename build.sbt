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
scalaVersion := "2.10.6"


// Library dependencies.
// ---------------------------------------
libraryDependencies ++= Seq(
   "org.webjars.npm" % "autoprefixer" % "6.7.7",
   "org.webjars.npm" % "postcss-cli" % "2.6.0",
   "org.webjars.npm" % "semver" % "5.3.0"
)


// Plugins.
// ---------------------------------------
addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.2.1")


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