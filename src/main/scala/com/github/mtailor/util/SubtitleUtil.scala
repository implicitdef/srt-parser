package com.github.mtailor.util

import java.io.InputStream

import com.github.mtailor.common.Vocabulary.SubtitleBlock
import com.github.mtailor.vttdissector.VttDissector
import org.htmlcleaner.HtmlCleaner

import scala.collection.mutable
import scala.util.{Failure, Success}

/**
  * Created by craiger on 14/05/18.
  */
object SubtitleUtil {

  val TIMED_TEXT = """<\d{2}:\d{2}:\d{2}\.\d{3}>""".r


  /**
    * Removes html tags and timing tags from caption text
    */
  def filterCaptionText(caption: String) = {
    val htmlCleaner = new HtmlCleaner()
    val root = htmlCleaner.clean(caption)
    val stripped = root.getText.toString
    val cleaned = TIMED_TEXT.replaceAllIn(stripped, "")
    cleaned

  }
  
  /**
    *
    * @param vtt
    * @param dedupeRollup WebVTT from YouTube is "timed text" using a "rollup" scheme. Essentially they duplicate
    *                     subtitle text between cues and add "timing" notation to tell the reader when to display that
    *                     a part of the text. Essentially any subtitle that has these timing codes will be in the second
    *                     line as it scrolls up from the top of the screen so we discard the first line and move on 
    * @return
    */
  def vttToSubtitles(vtt: InputStream, dedupeRollup:Boolean = false) : Seq[SubtitleBlock] = {
    VttDissector(vtt) match {
      case Failure(exception) => {
        println(exception)
        Nil
      }
      case Success(values) => {
        values.foldLeft(mutable.Stack[SubtitleBlock]()) { (result, vttElem: SubtitleBlock) => {
          // disregard the first line as the subtiles are scrolling up from the bottom
          val captionLines = vttElem.lines.tail
          if (dedupeRollup) {
            if (TIMED_TEXT.pattern.matcher(captionLines.mkString).find) {
              result.push(vttElem.copy(lines = captionLines))
            } else {
              val prev = result.pop()
              result.push(prev.copy(end = vttElem.end))
            }
          } else {
            result.push(vttElem)
          }
        }}.reverse.elems
      }
    }
  }
}
