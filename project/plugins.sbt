logLevel := Level.Warn


addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.4")


// Release builder, automates a large part of the release procedure.
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.10")