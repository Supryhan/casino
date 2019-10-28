import fastparse.all

name := "casino"

version := "0.1"

sbtVersion := "1.3.2"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http"   % "10.1.10",
    "com.typesafe.akka" %% "akka-stream" % "2.5.23",
    "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.23",
    "com.typesafe.akka" %% "akka-http-testkit" % "10.1.10",
    "org.scalatest" %% "scalatest-wordspec" % "3.2.0-M1",
    "org.scalatest" %% "scalatest" % "3.0.8",
)