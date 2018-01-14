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

  // Perform test
  logger.info("Perform test")
  master.predict(Vector(10.0, 20.0)) match {
    case Success(result) => logger.info(s"Result (1) is $result")
  }

  logger.info("Perform test")
  master.predict(Vector(10.0)) match {
    case Success(result) => logger.info(s"Result (2) is $result")
    case Failure(e) => logger.error(e.getMessage)
  }

  logger.info("Perform test")
  master.predict(Vector(10.0, 20.0, 30.0)) match {
    case Success(result) => logger.info(s"Result (3) is $result")
    case Failure(e) => logger.error(e.getMessage)
  }


  spark.close()
}
