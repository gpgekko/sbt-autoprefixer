sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("nl.semlab.sbt" % "sbt-autoprefixer" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

resolvers ++= Seq(
   Resolver.defaultLocal,
   Resolver.mavenLocal,
   "Semlab Snapshots Repository" at "http://192.168.150.190:8081/nexus/content/repositories/snapshots/"
)