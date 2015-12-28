import sbt._

name := "akka-ChatWorkTaskNotifier"
version := "1.0"
scalaVersion := "2.11.7"

enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
  "org.json4s" % "json4s-ext_2.11" % "3.3.0",
  "org.json4s" % "json4s-native_2.11" % "3.3.0",
  "org.json4s" % "json4s-jackson_2.11" % "3.3.0",
  "org.scalaj" %% "scalaj-http" % "2.2.0",
  "org.joda" % "joda-convert" % "1.8.1",
  "joda-time" % "joda-time" % "2.9.1",
  "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.0-M2",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.1",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % "2.0-M2"
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )


