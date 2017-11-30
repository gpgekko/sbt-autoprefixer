sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.github.gpgekko" % "sbt-autoprefixer" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

resolvers ++= Seq(
   Resolver.defaultLocal,
   Resolver.mavenLocal,
   Resolver.url("sbt snapshot plugins", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots"))(Resolver.ivyStylePatterns),
   Resolver.sonatypeRepo("snapshots"),
   "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
)