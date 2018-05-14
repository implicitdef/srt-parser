name := "srt-dissector"

organization := "com.github.mtailor"

version := "0.1.2"

scalaVersion := "2.11.12"

libraryDependencies += "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.1.0"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.22"

libraryDependencies += "org.specs2" %% "specs2" % "3.7" % Test


scalacOptions ++= Seq(
  "-feature",
  "-language:implicitConversions"
)
