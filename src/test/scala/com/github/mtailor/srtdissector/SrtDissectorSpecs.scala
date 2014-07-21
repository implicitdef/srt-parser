package com.github.mtailor.srtdissector

import com.github.mtailor.srtdissector.Vocabulary._
import org.specs2.mutable.Specification

object SrtDissectorSpecs extends Specification with FromClasspathLoader {

  val expectedSrtFromSample = Seq(
    SubtitleBlock(
      asTime(0, 0, 0, 125),
      asTime(0, 0, 2, 151),
      Seq(
        "<i>Previously on Mad Men...</i>"
      )
    ),
    SubtitleBlock(
      asTime(0, 0, 2, 34),
      asTime(0, 0, 3, 990),
      Seq(
        "You're going to Vietnam!"
      )
    ),
    SubtitleBlock(
      asTime(0, 0, 4, 781),
      asTime(0, 0, 6, 878),
      Seq(
        "I think I'd like eventually",
        "to do what you do."
      )
    ),
    SubtitleBlock(
      asTime(0, 0, 7, 0),
      asTime(0, 0, 8, 0),
      Seq(
        "You should get some sleep."
      )
    ),
    SubtitleBlock(
      asTime(0, 0, 9, 0),
      asTime(0, 0, 10, 0),
      Seq(
        "How can I?",
        "My wife's a big TV star."
      )
    ),
    SubtitleBlock(
      asTime(45, 55, 50, 999),
      asTime(45, 55, 51, 0),
      Seq(
        "Every chip I make we become",
        "less dependent on you!",
        "Why you...",
        "Roger..."
      )
    )
  )

  "SrtDissector" should {
    "parse properly a small but tricky .srt file" in {
      SrtDissector(file("sample.srt")) must beSuccessfulTry.withValue(expectedSrtFromSample)
    }
    "parse properly an empty .srt file" in {
      SrtDissector(file("empty.srt")) must beSuccessfulTry.which(_.isEmpty)
    }
    examplesBlock {
      files("srt_files") foreach { f =>
        "parse without failure the file " + f in {
          SrtDissector(f) must beSuccessfulTry
        }
      }
    }
  }

}
