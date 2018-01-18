package com.stulsoft.spark.research.regression

import org.apache.spark.sql.SparkSession

import scala.util.Try

/**
  * @author Yuriy Stul.
  */
trait Model {
  def buildModel(trainingData: Vector[(Double, Vector[Double])])(implicit sparkSession: SparkSession): Unit

  def saveModel(path: String): Try[Boolean]

  def loadModel(path: String): Try[Boolean]

  def predict(values: Vector[Double])(implicit sparkSession: SparkSession): Try[Double]
}
