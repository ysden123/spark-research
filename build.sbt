import sbt.Keys.libraryDependencies

lazy val sparkVersion = "2.2.1"
lazy val scalaLoggingVersion = "3.7.2"
lazy val scalaTestVersion = "3.0.4"

lazy val log4jVersion = "2.10.0"
lazy val log4jApiScalaVersion = "11.0"

lazy val commonSettings = Seq(
  organization := "com.stulsoft.log-analyzer",
  version := "1.1.2",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq(
    "-feature",
    "-language:implicitConversions",
    "-language:postfixOps"),
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
    "org.apache.logging.log4j" %% "log4j-api-scala" % log4jApiScalaVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )
)

//resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"
//resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
//resolvers += "Repo at github.com/ankurdave/maven-repo" at "https://github.com/ankurdave/maven-repo/raw/master"

lazy val sparkResearch = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "spark-research"
  )