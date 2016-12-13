name := "srt-dissector"

organization := "com.github.mtailor"

version := "0.1.2"

scalaVersion := "2.11.8"

libraryDependencies += "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.0.4"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "org.specs2" %% "specs2" % "3.7" % Test

scalacOptions ++= Seq(
  "-feature",
  "-language:implicitConversions"
)
