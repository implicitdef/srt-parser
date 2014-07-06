package com.github.mtailor.testscala

import scala.io.{BufferedSource, Source}
import java.io._

trait FromClasspathLoader {

  def fromClassPathFile(s: String): InputStream =
    getClass.getClassLoader.getResourceAsStream(s)

  def fromClassPathDirectory(s: String): Seq[File] =
    new File(getClass.getClassLoader.getResource(s).toURI).listFiles()

}
