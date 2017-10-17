lazy val root = Project("plugins", file(".")).dependsOn(plugin)

lazy val plugin = file("../").getCanonicalFile.toURI

resolvers ++= Seq(
  Resolver.defaultLocal,
  Resolver.mavenLocal,
  "Semlab Snapshots Repository" at "http://192.168.150.190:8081/nexus/content/repositories/snapshots/"
)