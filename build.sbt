import sbt.Keys.libraryDependencies

lazy val sparkVersion = "3.0.1"
lazy val scalaLoggingVersion = "3.9.2"
lazy val scalaTestVersion = "3.2.3"

lazy val loggingVersion = "2.13.3"

lazy val commonSettings = Seq(
  organization := "com.stulsoft.spark-research",
  version := "1.0.2",
  scalaVersion := "2.12.12",
  scalacOptions ++= Seq(
    "-feature",
    "-language:implicitConversions",
    "-language:postfixOps"),
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-mllib" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,

    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,

    "org.apache.logging.log4j" % "log4j-api" % loggingVersion,
    "org.apache.logging.log4j" % "log4j-core" % loggingVersion,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % loggingVersion,

    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )
)

lazy val sparkResearch = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "spark-research"
  )

parallelExecution in Test := false
