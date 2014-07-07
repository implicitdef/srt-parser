package com.github.mtailor.srtdissector

import java.io.Reader

import com.github.mtailor.srtdissector.Vocabulary._

import scala.util.parsing.combinator.RegexParsers

/**
 * Implementation of the parser, based on parsers combinators
 */
object SrtParsers extends RegexParsers {

  def doFullParsing(reader: Reader): Srt =
    parseAll(srt, reader) match {
      case Success(res, _) => res
      case noSuccess => throw new ParsingException(
        "Failed to parse the given source" +
          " as a .srt file : " + noSuccess)
    }

  override val skipWhitespace = false

  private def srt: Parser[Srt] =
    ows ~> repsep(subtitleBlock, blockSeparator) <~ ows ^^ {
      case subtitleBlocks =>
        subtitleBlocks
          //blocks should have one line at least
          .filter(! _.lines.isEmpty)
          //blocks should have coherent times
          .filter(b => b.start < b.end)
          //and should be in chronological order
          .sortBy(_.start)
    }

  private def subtitleBlock: Parser[SubtitleBlock] = {
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
    eol <~ ows

  private def subtitleNumber: Parser[Int] =
    """\d+""".r ^^ (_.toInt)

  private def time: Parser[Time] =
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

  //optional whitespaces shortcut
  private def ows : Parser[Any] =
    whiteSpace.?

}
