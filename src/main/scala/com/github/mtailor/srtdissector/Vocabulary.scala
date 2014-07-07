package com.github.mtailor.srtdissector


object Vocabulary {

  //The result of parsing a .srt file
  type Srt = Seq[SubtitleBlock]

  //One subtitle from a .srt file
  //with several lines to be displayed
  //between a start time and a end time
  case class SubtitleBlock (
    start: Time,
    end: Time,
    lines: Seq[String]
  )

  //times are simply represented as the number of milliseconds
  //since the beginning
  type Time = Int

  // user-friendly translation
  def asTime(hours: Int, minutes: Int, seconds: Int, milliSeconds: Int): Time =
      hours   * 1000 * 60 * 60 +
      minutes * 1000 * 60 +
      seconds * 1000 +
      milliSeconds


  //thrown when the parsing did not succeed
  class ParsingException(msg: String) extends RuntimeException(msg)


}
