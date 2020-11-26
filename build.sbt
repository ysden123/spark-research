import sbt.Keys.libraryDependencies

lazy val sparkVersion = "3.0.1"
lazy val scalaLoggingVersion = "3.9.2"
lazy val scalaTestVersion = "3.2.3"

lazy val log4jVersion = "2.14.0"
lazy val log4jApiScalaVersion = "12.0"

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
    "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
    "org.apache.logging.log4j" %% "log4j-api-scala" % log4jApiScalaVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )
)

lazy val sparkResearch = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "spark-research"
  )

parallelExecution in Test := false