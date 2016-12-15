sbt-autoprefixer
================

[![Build Status](https://travis-ci.org/gpgekko/sbt-autoprefixer.svg?branch=master)](https://travis-ci.org/gpgekko/sbt-autoprefixer)

[sbt-web](https://github.com/sbt/sbt-web) plugin that uses [postcss-cli](https://github.com/postcss/postcss-cli) to run [Autoprefixer](https://github.com/postcss/autoprefixer) to 
post-process CSS and add vendor prefixes to rules by [Can I Use](http://caniuse.com).

To use the latest version from Github, add the following to the `project/plugins.sbt` of your project:

```scala
lazy val sbtAutoprefixer = uri("git://github.com/gpgekko/sbt-autoprefixer")
lazy val root = project.in(file(".")).dependsOn(sbtAutoprefixer)
```

Your project's build file also needs to enable sbt-web plugins. For example with `build.sbt`:

```scala
lazy val root = (project in file(".")).enablePlugins(SbtWeb)
```

Declare the execution order of the pipeline:
```scala
pipelineStages := Seq(autoprefixer)
```

**Note:** if dependency resolving breaks on `org.webjars.npm.semver`, add the following line to the `project/plugins.sbt`:
```scala
dependencyOverrides += "org.webjars.npm" % "semver" % "5.3.0"
```


Options
-------

The following options are supported:

Option              | Description
--------------------|------------
browsers            | Which [browsers Autoprefixer will support](https://github.com/ai/browserslist#queries). Default: `> 1% in NL, last 2 versions, Firefox ESR`
config              | Contents of the [config](https://github.com/postcss/autoprefixer#options) [file](https://github.com/postcss/postcss-cli#--config-c). Use this if you want to fine tune the Autoprefixer configuration. Default: the browsers provided above
    
The following sbt code illustrates how to set the browsers

```scala
browsers in autoprefixer := JS.Array("> 5%", "last 4 versions")
```

To include just one CSS file for post processing

```scala
includeFilter in autoprefixer := GlobFilter("mystyle.css")
```
