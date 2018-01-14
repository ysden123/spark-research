/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.spark.research

import com.stulsoft.spark.research.data.generator.LinearFunction
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

/** Runs Linear Regression model
  *
  * @author Yuriy Stul
  */
object LinearRegressionModelRunner extends App with LazyLogging {
  val spark: SparkSession = SparkSession.builder
    .master("local")
    .appName("Linear Regression model")
    .getOrCreate()

  import spark.implicits._

  // Getting data for training
  logger.info("Getting data for training")
  val trainingData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    .map(d => LabeledPoint(d._1, Vectors.dense(d._2.toArray)))
    .toDF


  // Build model
  logger.info("Building model ...")
  val lr = new LinearRegression
  val model = lr.fit(trainingData)

  logger.info(s"model.coefficients: ${model.coefficients}")


  // Perform test
  logger.info("Perform test")
  val testData = Vector(LabeledPoint(11.0, Vectors.dense(10.0, 20.0))).toDF()
  val result = model.transform(testData)
  result.show

  spark.close()
}
