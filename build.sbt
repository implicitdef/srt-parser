name := "srt-dissector"

organization := "com.github.mtailor"

version := "0.1"

scalaVersion := "2.11.1"

libraryDependencies += "org.scala-lang" % "scala-parser-combinators" % "2.11.0-M4"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.12" % Test

scalacOptions ++= Seq(
  "-feature",
  "-language:implicitConversions"
)
