logLevel := Level.Warn


libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value


addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.1")


// Release builder, automates a large part of the release procedure.
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")