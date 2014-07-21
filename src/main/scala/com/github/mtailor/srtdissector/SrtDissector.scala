package com.github.mtailor.srtdissector

import java.io.{InputStream, Reader}

import com.github.mtailor.srtdissector.Vocabulary._
import org.apache.commons.io.input.BOMInputStream

import scala.io._
import scala.util.Try

/**
 * The trait/object offering the final API.
 * Parses the InputStream from a UTF8-encoded .srt file
 */
object SrtDissector extends SrtDissector


trait SrtDissector extends (InputStream => Try[Srt]) {


  override def apply(is: InputStream): Try[Srt] =
    SrtParsers.doFullParsing(withoutBom(is))


  private def withoutBom(is: InputStream) =
    // use commmons-io to handle the BOM
    new BOMInputStream(is)

  private implicit def inputStream2Reader(is: InputStream): Reader =
    Source.fromInputStream(is)(Codec.UTF8).bufferedReader()

}
