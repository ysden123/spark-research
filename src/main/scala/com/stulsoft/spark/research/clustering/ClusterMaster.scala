package com.stulsoft.spark.research.clustering

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

import scala.util.{Failure, Success, Try}

/**
  * @author Yuriy Stul.
  */
class ClusterMaster extends LazyLogging {
  private var model: KMeansModel = _

  def buildModel(data: Vector[Vector[Double]])(implicit sparkSession: SparkSession): Unit = {
    logger.info("Building model")
    require(data != null && data.nonEmpty, "data should be specified")
    import sparkSession.implicits._
    model = (new KMeans).fit(data.map(d => LabeledPoint(0.0, Vectors.dense(d.toArray))).toDF)
    logger.info("Built model")
  }

  /**
    * Finds an appropriate cluster
    *
    * @param data         input data
    * @param sparkSession the Spark session
    * @return the id of the appropriated cluster
    */
  def predict(data: Vector[Double])(implicit sparkSession: SparkSession): Try[Int] = {
    if (model == null) {
      logger.error("Model doesn't exist. Call buildModel before predict")
      Failure(new IllegalStateException("A model was not created"))
    } else {
      import sparkSession.implicits._
      Success(
        model
          .transform(Vector(data).map(d => LabeledPoint(0.0, Vectors.dense(d.toArray))).toDF)
          .collect()
          .head
          .getAs[Int](2)
      )
    }
  }

  def describe(): Try[Vector[String]] = {
    if (model == null) {
      logger.error("Model doesn't exist. Call buildModel before predict")
      Failure(new IllegalStateException("A model was not created"))
    } else {
      Success(Vector(
        s"""clusterCenters: ${model.clusterCenters.mkString(", ")}"""
      ))
    }
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

  def loadModel(path: String): Try[Boolean] = {
    require(path != null && !path.isEmpty, "path should be specified")
    try {
      logger.info(s"Loading model from $path")
      model = KMeansModel.load(path)
      logger.info(s"Loaded model from $path")
      Success(true)
    }
    catch {
      case e: Throwable =>
        logger.error(s"Failed loading model from $path Error: ${e.getMessage}")
        Failure(e)
    }
  }
}
