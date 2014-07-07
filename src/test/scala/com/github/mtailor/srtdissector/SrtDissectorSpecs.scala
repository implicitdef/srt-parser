package com.github.mtailor.srtdissector

import com.github.mtailor.srtdissector.Vocabulary._
import org.specs2.mutable.Specification

object SrtDissectorSpecs extends Specification with SrtDissector with FromClasspathLoader {

  val expectedSrtFromSample = new Srt(Seq(
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
      asTime(45, 55, 50, 999),
      asTime(45, 55, 51, 0),
      Seq(
        "Every chip I make we become",
        "less dependent on you!",
        "Why you...",
        "Roger..."
      )
    ))
  )

  //TODO gérer les cas tricky des times : debut > fin, blocks mal triés
  //TODO gérer le cas où il ne reste plus aucun subtitle après filtrage

  "SrtDissector" should {
    "parse properly a small but tricky .srt file" in {
      dissect(file("sample.srt")) mustEqual expectedSrtFromSample
    }
    examplesBlock {
      files("srt_files") foreach { f =>
        "parse without failure the file " + f in {
          dissect(f) must not(throwA[ParsingException])
        }
      }
    }
  }

}
