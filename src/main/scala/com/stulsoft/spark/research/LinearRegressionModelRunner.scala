/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.spark.research

import com.stulsoft.spark.research.data.generator.LinearFunction
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

import scala.util.{Failure, Success}

/** Runs Linear Regression model
  *
  * @author Yuriy Stul
  */
object LinearRegressionModelRunner extends App with LazyLogging {
  val spark: SparkSession = SparkSession.builder
    .master("local")
    .appName("Linear Regression model")
    .getOrCreate()

  // Getting data for training
  logger.info("Getting data for training")
  val trainingData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)

  val master = new RegressionModelMaster(spark)
  val model = master.buildModel(trainingData)
  logger.info(s"model.coefficients: ${model.coefficients}, model.numFeatures = ${model.numFeatures}")
  logger.info(s"model.intercept: ${model.intercept}")
  val summary = model.summary
  logger.info(s"Coefficient Standard Errors: ${summary.coefficientStandardErrors.mkString(",")}")
  logger.info(s"T Values: ${summary.tValues.mkString(",")}")
  logger.info(s"P Values: ${summary.pValues.mkString(",")}")
  logger.info(s"summary.totalIterations: ${summary.totalIterations}")

  // Perform test
  logger.info("Perform test")
  master.predict(Vector(10.0, 20.0)) match {
    case Success(result) => logger.info(s"Result (1) is $result")
    case Failure(e) => logger.error(s"Failed prediction. Error is ${e.getMessage}")
  }

  logger.info("Perform test")
  master.predict(Vector(10.0)) match {
    case Success(result) => logger.info(s"Result (2) is $result")
    case Failure(e) => logger.error(s"Failed prediction (2). Error is ${e.getMessage}")
  }

  logger.info("Perform test")
  master.predict(Vector(10.0, 20.0, 30.0)) match {
    case Success(result) => logger.info(s"Result (3) is $result")
    case Failure(e) => logger.error(s"Failed prediction (3). Error is ${e.getMessage}")
  }


  spark.close()
}
