logLevel := Level.Warn


libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value


// Release builder, automates a large part of the release procedure.
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.8")