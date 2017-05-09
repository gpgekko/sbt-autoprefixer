{
  val pluginVersion = System.getProperty("plugin.version")
  if(pluginVersion == null)
    throw new RuntimeException("""|The system property 'plugin.version' is not defined.
                                  |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
  else addSbtPlugin("com.github.gpgekko" % "sbt-autoprefixer" % pluginVersion)
}

dependencyOverrides += "org.webjars.npm" % "block-stream" % "0.0.9"