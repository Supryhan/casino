import fastparse.all

name := "casino"

version := "0.1"

sbtVersion := "1.3.2"

scalaVersion := "2.13.1"

val AkkaVersion = "2.5.23"
val AkkaHttpVersion = "10.1.10"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http"   % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
    "org.scalatest" %% "scalatest-wordspec" % "3.2.0-M1",
    "org.scalatest" %% "scalatest" % "3.0.8",
    "org.specs2" %% "specs2-core" % "4.5.1",
)