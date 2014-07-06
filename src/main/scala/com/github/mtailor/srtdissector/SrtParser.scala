package com.github.mtailor.srtdissector

import java.io.InputStream

import com.github.mtailor.srtdissector.Vocabulary._
import org.apache.commons.io.input.BOMInputStream

import scala.io._
import scala.util.parsing.combinator.RegexParsers

object SrtParser {

  def parse(is: InputStream): Srt =
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
      whiteSpace.? ~> rep1sep(subtitleBlock, blockSeparator) <~ whiteSpace.? ^^ {
        case subtitleBlocks =>
          subtitleBlocks.filterNot(_.lines.isEmpty)
      }

    def subtitleBlock: Parser[SubtitleBlock] = {
      subtitleNumber ~ whiteSpace.? ~
      time ~ arrow ~ time ~ whiteSpace.? ~
      textLines
    } ^^ {
      case
        _ ~ _ ~
        startTime ~ _ ~ endTime ~ _ ~
        texts
      => SubtitleBlock(startTime, endTime, texts)
    }

    private def textLines: Parser[Seq[String]] =
      repsep(textLine, eol)

    private def textLine: Parser[String] =
      """.+""".r

    private def eol: Parser[Any] =
      //\n is unix
      //\r\n is windows
      //\r appears in some broken files
      "\n" | "\r\n" | "\r"

    private def blockSeparator: Parser[Any] =
      eol <~ whiteSpace.?

    private def subtitleNumber: Parser[Int] =
      """\d+""".r ^^ (_.toInt)

    private def time: Parser[Time] =
      hours ~ whiteSpace.? ~ timeSep ~
      whiteSpace.? ~ minutes ~ whiteSpace.? ~ timeSep ~
      whiteSpace.? ~ seconds ~ whiteSpace.? ~ timeSep ~
      whiteSpace.? ~ milliseconds ^^
      {
        case
              h ~ _~ _ ~
          _ ~ m ~ _ ~ _ ~
          _ ~ s ~ _ ~ _ ~
          _ ~ ms =>
          asTime(h, m, s, ms)
      }

    private def arrow: Parser[Any] =
      """\s*-->\s*""".r

    private def timeSep: Parser[Any] =
      ":" | ","

    private def hours: Parser[Int] =
      aFewNumbers

    private def minutes: Parser[Int] =
      aFewNumbers withFilter (_ < 60)

    private def seconds =
      aFewNumbers withFilter (_ < 60)

    private def milliseconds: Parser[Int] =
      aFewNumbers

    private def aFewNumbers: Parser[Int] =
      """\d{1,4}""".r ^^ (_.toInt)

  }



}
