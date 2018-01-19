/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.spark.research.regression

import org.apache.spark.sql.SparkSession

import scala.util.Try

/** Regression model manager
  *
  * @author Yuriy Stul.
  */
trait ModelMaster {
  this: Model =>
  def buildModel(trainingData: Vector[(Double, Vector[Double])])(implicit sparkSession: SparkSession): Unit = buildModel(trainingData)

  def saveModel(path: String): Try[Boolean] = saveModel(path)

  def loadModel(path: String): Try[Boolean] = loadModel(path)

  def predict(values: Vector[Double])(implicit sparkSession: SparkSession): Try[Double] = predict(values)
}
