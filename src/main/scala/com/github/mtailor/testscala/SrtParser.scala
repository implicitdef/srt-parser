package com.github.mtailor.testscala

import java.io.InputStream

import com.github.mtailor.testscala.Vocabulary._
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
        case subtitleBlocks => subtitleBlocks
      }

    def subtitleBlock: Parser[SubtitleBlock] = {
      subtitleNumber ~ eol ~
      time ~ arrow ~ time ~ eol ~
      textLines
    } ^^ {
      case
        _ ~ _ ~
        startTime ~ _ ~ endTime ~ _ ~
        texts
      => SubtitleBlock(startTime, endTime, texts)
    }

    private def textLines: Parser[Seq[String]] =
      rep1sep(textLine, eol)

    private def textLine: Parser[String] =
      """.+""".r

    private def eol: Parser[Any] =
      """\r?\n""".r

    private def blockSeparator: Parser[Any] =
      eol <~ opt(whiteSpace)

    private def subtitleNumber: Parser[Int] =
      """\d+""".r ^^ (_.toInt)

    private def time: Parser[Time] =
      hours ~ ":" ~ minutes ~ ":" ~ seconds ~ "," ~ milliseconds ^^ {
        case h ~ _ ~ m ~ _ ~ s ~ _ ~ ms =>
          asTime(h, m, s, ms)
      }

    private def arrow: Parser[Any] =
      """\s*-->\s*""".r

    private def hours: Parser[Int] =
      """\d{1,2}""".r ^^ (_.toInt)

    private def minutes: Parser[Int] =
      """\d{1,2}""".r ^^ (_.toInt) withFilter (_ < 60)

    private def seconds =minutes

    private def milliseconds: Parser[Int] =
      """\d{1,3}""".r ^^ (_.toInt)


  }



}
