package com.github.mtailor.test.util

import com.github.mtailor.common.Vocabulary.SubtitleBlock
import com.github.mtailor.test.FromClasspathLoader
import com.github.mtailor.util.SubtitleUtil
import org.specs2.mutable.Specification

object SubtitleUtilSpecs extends Specification with FromClasspathLoader {


  "SubtitleUtil#filterCaptionText" should {
    "\tstrip tags & timings out of the captions" in {
      lazy val result = SubtitleUtil.vttToSubtitles(file("vtt_files/sample_tags.vtt"))
      result mustNotEqual null
      val strippedResult = result.map(s => s.copy(lines = List(SubtitleUtil.filterCaptionText(s.lines.head))))
      strippedResult must beEqualTo(SubtitleData.expectedStrippedTagsSample)
    }
  }
  
  object SubtitleData {
    val expectedStrippedTagsSample = Seq(
      SubtitleBlock(30, 1610, List(" all right we are here in sunny Los")),
      SubtitleBlock(1610, 1620, List("Angeles and we're gonna be doing episode"))
    )
  }
}
