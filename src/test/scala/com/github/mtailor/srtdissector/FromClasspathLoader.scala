package com.github.mtailor.srtdissector

import scala.io.{BufferedSource, Source}
import java.io._

trait FromClasspathLoader {

  def file(filePathInClassPath: String): File =
    new File(getClass.getClassLoader.getResource(filePathInClassPath).toURI)

  def files(directoryPathInClassPath: String): Seq[File] =
    new File(getClass.getClassLoader.getResource(directoryPathInClassPath).toURI).listFiles()

  implicit def file2InputStream(f: File): FileInputStream =
    new FileInputStream(f)

}
