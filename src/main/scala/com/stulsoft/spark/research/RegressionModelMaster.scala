/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.spark.research

import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.sql.SparkSession

import scala.util.{Failure, Success, Try}

/**
  * @author Yuriy Stul
  */
class RegressionModelMaster(val sparkSession: SparkSession) {
  require(sparkSession != null, "sparkSession should bew specified")

  import sparkSession.implicits._

  private var model: LinearRegressionModel = _

  def buildModel(trainingData: Vector[(Double, Vector[Double])]): LinearRegressionModel = {
    require(trainingData != null && trainingData.nonEmpty, "trainingData should be specified")
    val lr = new LinearRegression
    model = lr.fit(trainingData.map(d => LabeledPoint(d._1, Vectors.dense(d._2.toArray))).toDF)
    model
  }

  def saveModel(path: String): Try[Boolean] = {
    require(path != null && !path.isEmpty, "path should be specified")
    if (model == null)
      Failure(new IllegalStateException("A model was not created"))
    else {
      try {
        model.write.overwrite.save(path)
        Success(true)
      }
      catch {
        case e: Throwable => Failure(e)
      }
    }
  }

  def loadModel(path: String): Try[LinearRegressionModel] = {
    try {
      model = LinearRegressionModel.load(path)
      Success(model)
    }
    catch {
      case e: Throwable => Failure(e)
    }
  }

  def predict(values: Vector[Double]): Try[Double] = {
    require(values != null && values.nonEmpty, "values should be specified")
    if (model == null)
      Failure(new IllegalStateException("A model was not created"))
    else {
      Try {
        model
          .transform(Vector(LabeledPoint(0.0, Vectors.dense(values.toArray))).toDF)
          .head()
          .getAs[Double]("prediction")
      }
    }
  }

}
