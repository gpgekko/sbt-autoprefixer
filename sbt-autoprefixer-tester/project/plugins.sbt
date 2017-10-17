lazy val root = Project("plugins", file(".")).dependsOn(plugin)

lazy val plugin = file("../").getCanonicalFile.toURI

resolvers ++= Seq(
  Resolver.defaultLocal,
  Resolver.mavenLocal
)