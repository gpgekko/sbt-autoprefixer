package nl.semlab.sbt.autoprefixer

import com.typesafe.sbt.web.Compat.CacheStore
import sbt.Keys.TaskStreams

/**
  * Compatibility object for stuff that differs between the SBT versions we crossbuild.
  * <p>
  * Project sbt-autoprefixer<br>
  * Compat.java created aug 25, 2017
  * <p>
  * Copyright &copy; 2017 SemLab
  *
  * @author <a href="mailto:pot@semlab.nl">G. Pot</a>
  */
object Compat {
   def cacheStore(stream: TaskStreams, identifier: scala.Predef.String): CacheStore = {
      com.typesafe.sbt.web.Compat.cacheStore(stream, identifier)
   }

   object DummyPath

   val SbtIoPath: DummyPath.type = DummyPath
}