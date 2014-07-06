package com.github.mtailor.srtdissector

import java.io.InputStream

import com.github.mtailor.srtdissector.Vocabulary._
import org.apache.commons.io.input.BOMInputStream

import scala.io._
import scala.util.parsing.combinator.RegexParsers

object SrtDissector {

  /**
   * Parses the InputStream from a .srt file
   * Throws ParsingException if it fails
   */
  def dissect(is: InputStream): Srt =
    // use commmons-io to handle the BOM
    SrtParsers.doFullParsing(Source.fromInputStream(new BOMInputStream(is)))

  private object SrtParsers extends RegexParsers {

    def doFullParsing(source: BufferedSource): Srt = {
      parseAll(srt, source.bufferedReader()) match {
        case Success(res, _) => res
        case noSuccess =>
          throw new ParsingException("Failed to parse the given source" +
            " as a .srt file : " + noSuccess)
      }
    }

    override val skipWhitespace = false

    def srt: Parser[Srt] =
      ows ~> rep1sep(subtitleBlock, blockSeparator) <~ ows ^^ {
        case subtitleBlocks =>
          subtitleBlocks.filterNot(_.lines.isEmpty)
      }

    def subtitleBlock: Parser[SubtitleBlock] = {
      subtitleNumber ~ ows ~
      time ~ arrow ~ time ~ ows ~
      textLines
    } ^^ {
      case
        _ ~ _ ~
        startTime ~ _ ~ endTime ~ _ ~
        texts
      => SubtitleBlock(startTime, endTime, texts)
    }

    def textLines: Parser[Seq[String]] =
      repsep(textLine, eol)

    def textLine: Parser[String] =
      """.+""".r

    def eol: Parser[Any] =
      //\n is unix
      //\r\n is windows
      //\r appears in some broken files
      "\n" | "\r\n" | "\r"

    def blockSeparator: Parser[Any] =
      eol <~ ows

    def subtitleNumber: Parser[Int] =
      """\d+""".r ^^ (_.toInt)

    def time: Parser[Time] =
      hours ~ ows ~ timeSep ~
      ows ~ minutes ~ ows ~ timeSep ~
      ows ~ seconds ~ ows ~ timeSep ~
      ows ~ milliseconds ^^
      {
        case
              h ~ _~ _ ~
          _ ~ m ~ _ ~ _ ~
          _ ~ s ~ _ ~ _ ~
          _ ~ ms =>
          asTime(h, m, s, ms)
      }

    def arrow: Parser[Any] =
      """\s*-->\s*""".r

    def timeSep: Parser[Any] =
      ":" | ","

    def hours: Parser[Int] =
      aFewNumbers

    def minutes: Parser[Int] =
      aFewNumbers withFilter (_ < 60)

    def seconds =
      aFewNumbers withFilter (_ < 60)

    def milliseconds: Parser[Int] =
      aFewNumbers

    def aFewNumbers: Parser[Int] =
      """\d{1,4}""".r ^^ (_.toInt)

    //optional whitespaces shortcut
    def ows : Parser[Any] =
      whiteSpace.?

  }



}
