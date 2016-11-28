logLevel := Level.Warn

libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value

// Update checker, searches repositories for newer versions of dependencies.
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.0")