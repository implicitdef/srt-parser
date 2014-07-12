package com.github.mtailor.srtdissector

import java.io.{InputStream, Reader}

import com.github.mtailor.srtdissector.Vocabulary._
import org.apache.commons.io.input.BOMInputStream

import scala.io._

/**
 * The trait offering the final API
 */
trait SrtDissector {

  /**
   * Parses the InputStream from a UTF8-encoded .srt file
   * Throws ParsingException if it fails
   */
  def dissect(is: InputStream): Srt =
    SrtParsers.doFullParsing(withoutBom(is))

  class ParsingException(msg: String) extends RuntimeException(msg)

  private def withoutBom(is: InputStream) =
    // use commmons-io to handle the BOM
    new BOMInputStream(is)

  private implicit def inputStream2Reader(is: InputStream): Reader =
    Source.fromInputStream(is)(Codec.UTF8).bufferedReader()

}
