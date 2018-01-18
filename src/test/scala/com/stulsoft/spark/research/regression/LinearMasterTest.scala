package com.stulsoft.spark.research.regression

import com.stulsoft.spark.research.data.generator.LinearFunction
import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import scala.util.{Failure, Success}

/**
  * @author Yuriy Stul.
  */
class LinearMasterTest extends FlatSpec with BeforeAndAfterEach with Matchers {
  val modelPath = "linear-master-tTest"
  implicit var sparkSession: SparkSession = _

  override def beforeEach() {
    super.beforeEach()
    sparkSession = SparkSession.builder
      .master("local")
      .appName("Regression Model Master test")
      .getOrCreate()
  }

  override def afterEach() {
    super.afterEach()
    sparkSession.close()
  }

  behavior of "LinearMaster"

  "buildModel" should "build model" in {
    val master = new ModelMaster with LinearMaster
    val experimentalData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    master.buildModel(experimentalData)
  }

  "loadModel" should "load model" in {
    val master = new ModelMaster with LinearMaster
    val experimentalData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    master.buildModel(experimentalData)
    master.saveModel(modelPath) match {
      case Success(r) => r shouldBe true
      case Failure(e) => fail(e.getMessage)
    }
    master.loadModel(modelPath) match {
      case Success(r) => r shouldBe true
      case Failure(e) => fail(e.getMessage)
    }
  }

  "saveModel" should "save model" in {
    val master = new ModelMaster with LinearMaster
    val experimentalData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    master.buildModel(experimentalData)
    master.saveModel(modelPath) match {
      case Success(r) => r shouldBe true
      case Failure(e) => fail(e.getMessage)
    }
  }

  it should "predict" in {
    val master = new ModelMaster with LinearMaster
    val experimentalData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    master.buildModel(experimentalData)
    master.predict(Vector(10.0, 20)) match {
      case Success(prediction) => Math.abs(prediction - 60.0) * 100.0 / 60.0 should be <= 1.0
      case Failure(e) => fail(e.getMessage)
    }
  }

}