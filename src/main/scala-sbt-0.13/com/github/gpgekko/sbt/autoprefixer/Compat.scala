package com.github.gpgekko.sbt.autoprefixer

import com.typesafe.sbt.web.Compat.CacheStore
import sbt.Keys.TaskStreams

object Compat {
   def cacheStore(stream: TaskStreams, identifier: scala.Predef.String): CacheStore = {
      com.typesafe.sbt.web.Compat.cacheStore(stream, identifier)
   }

   object DummyPath

   val SbtIoPath: DummyPath.type = DummyPath
}
