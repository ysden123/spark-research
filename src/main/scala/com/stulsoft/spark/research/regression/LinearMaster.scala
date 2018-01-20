/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.spark.research.regression

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.sql.SparkSession

import scala.util.{Failure, Success, Try}

/** Linear regression model implementation
  *
  * @author Yuriy Stul.
  */
trait LinearMaster extends Model with LazyLogging {
  private var model: LinearRegressionModel = _

  override def buildModel(trainingData: Vector[(Double, Vector[Double])])(implicit sparkSession: SparkSession): Unit = {
    logger.info("Building model")
    require(trainingData != null && trainingData.nonEmpty, "trainingData should be specified")
    import sparkSession.implicits._
    val lr = new LinearRegression
    model = lr.fit(trainingData.map(d => LabeledPoint(d._1, Vectors.dense(d._2.toArray))).toDF)
    logger.info("Built model")
  }

  override def saveModel(path: String): Try[Boolean] = {
    require(path != null && !path.isEmpty, "path should be specified")
    if (model == null) {
      logger.error("Model doesn't exist. Call buildModel before predict")
      Failure(new IllegalStateException("A model was not created"))
    } else {
      logger.info(s"Saving model into $path")
      try {
        model.write.overwrite.save(path)
        logger.info(s"Saved model into $path")
        Success(true)
      }
      catch {
        case e: Throwable =>
          logger.error(s"Failed saving model into $path Error: ${e.getMessage}")
          Failure(e)
      }
    }
  }

  override def loadModel(path: String): Try[Boolean] = {
    require(path != null && !path.isEmpty, "path should be specified")
    try {
      logger.info(s"Loading model from $path")
      model = LinearRegressionModel.load(path)
      logger.info(s"Loaded model from $path")
      Success(true)
    }
    catch {
      case e: Throwable =>
        logger.error(s"Failed loading model from $path Error: ${e.getMessage}")
        Failure(e)
    }
  }

  override def predict(values: Vector[Double])(implicit sparkSession: SparkSession): Try[Double] = {
    require(values != null && values.nonEmpty, "values should be specified")
    if (model == null) {
      logger.error("Model doesn't exist. Call buildModel before predict")
      Failure(new IllegalStateException("A model was not created"))
    } else {
      import sparkSession.implicits._
      if (values.length != model.numFeatures)
        Failure(new IllegalArgumentException(s"RegressionModelMaster.predict: number of values ${values.length} should be equal to ${model.numFeatures}"))
      else
        Try {
          model
            .transform(Vector(LabeledPoint(0.0, Vectors.dense(values.toArray))).toDF)
            .head()
            .getAs[Double]("prediction")
        }
    }
  }

  override def describe(): Try[Vector[String]] = {
    if (model == null) {
      logger.error("Model doesn't exist. Call buildModel before predict")
      Failure(new IllegalStateException("A model was not created"))
    } else {
      Success(Vector(
        "coefficients: " + model.coefficients.toArray.mkString(", "),
        "intercept = " + model.intercept,
        model.explainParams()
      ))
    }
  }
}
