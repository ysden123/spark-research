/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.spark.research

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.{GeneralizedLinearRegression, GeneralizedLinearRegressionModel}
import org.apache.spark.sql.SparkSession

import scala.util.{Failure, Success, Try}

/**
  * @author Yuriy Stul.
  */
class GeneralizedLinearRegressionMaster(val sparkSession: SparkSession) extends LazyLogging {
  require(sparkSession != null, "sparkSession should bew specified")

  import sparkSession.implicits._

  private var model: GeneralizedLinearRegressionModel = _

  def buildModel(trainingData: Vector[(Double, Vector[Double])]): GeneralizedLinearRegressionModel = {
    logger.info("Building model")
    require(trainingData != null && trainingData.nonEmpty, "trainingData should be specified")
    val lr = new GeneralizedLinearRegression()
      .setFamily("gaussian")

    model = lr.fit(trainingData.map(d => LabeledPoint(d._1, Vectors.dense(d._2.toArray))).toDF)

    logger.info("Built model")
    model
  }

  def saveModel(path: String): Try[Boolean] = {
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

  def loadModel(path: String): Try[GeneralizedLinearRegressionModel] = {
    try {
      logger.info(s"Loading model from $path")
      model = GeneralizedLinearRegressionModel.load(path)
      logger.info(s"Loaded model from $path")
      Success(model)
    }
    catch {
      case e: Throwable =>
        logger.error(s"Failed loading model from $path Error: ${e.getMessage}")
        Failure(e)
    }
  }

  def predict(values: Vector[Double]): Try[Double] = {
    require(values != null && values.nonEmpty, "values should be specified")
    if (model == null) {
      logger.error("Model doesn't exist. Call buildModel before predict")
      Failure(new IllegalStateException("A model was not created"))
    } else {
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

}
