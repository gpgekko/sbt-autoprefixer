logLevel := Level.Warn

libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")


// Release builder, automates a large part of the release procedure.
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.4")

// Update checker, searches repositories for newer versions of dependencies.
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.0")