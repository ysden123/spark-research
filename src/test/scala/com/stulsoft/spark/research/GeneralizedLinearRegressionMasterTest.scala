/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.spark.research

import com.stulsoft.spark.research.data.generator.LinearFunction
import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import scala.util.{Failure, Success}

/**
  * @author Yuriy Stul.
  */
class GeneralizedLinearRegressionMasterTest extends FlatSpec with Matchers with BeforeAndAfterEach {
  val modelPath = "target/models/generalized-regression-model-test"
  var sparkSession: SparkSession = _

  behavior of "GeneralizedLinearRegressionMaster"

  "saveModel" should "save model" in {
    val experimentalData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    val m = new GeneralizedLinearRegressionMaster(sparkSession)
    val _ = m.buildModel(experimentalData)
    m.saveModel(modelPath) match {
      case Success(r) => r shouldBe true
      case Failure(e) => fail(e.getMessage)
    }
  }

  "predict" should "handle invalid values" in {
    val m = new GeneralizedLinearRegressionMaster(sparkSession)
    assertThrows[IllegalArgumentException] {
      m.predict(null)
    }
    assertThrows[IllegalArgumentException] {
      m.predict(Vector())
    }
  }

  it should "handle missing model" in {
    val m = new GeneralizedLinearRegressionMaster(sparkSession)
    val result = m.predict(Vector(1.0, 2.0))
    result match {
      case Success(_) =>
        fail("cannot work with missed model")
      case Failure(x) =>
        //        println(x.getMessage)
        succeed
    }
  }

  it should "return correct prediction" in {
    val experimentalData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    val m = new GeneralizedLinearRegressionMaster(sparkSession)
    val model = m.buildModel(experimentalData)
    //    println(s"model.coefficients: ${model.coefficients}")
    //    println(s"model.summary.numIterations: ${model.summary.numIterations}")

    m.predict(Vector(10.0, 20)) match {
      case Success(prediction) =>
        //        println(s"prediction=$prediction")
        Math.abs(prediction - 60.0) * 100.0 / 60.0 should be <= 1.0
      case Failure(e) => fail(e.getMessage)
    }
  }

  it should "return correct prediction in loaded model" in {
    val experimentalData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    val m1 = new GeneralizedLinearRegressionMaster(sparkSession)
    m1.buildModel(experimentalData)
    m1.saveModel(modelPath)

    val m2 = new GeneralizedLinearRegressionMaster(sparkSession)
    m2.loadModel(modelPath)
    m2.predict(Vector(10.0, 20)) match {
      case Success(prediction) => Math.abs(prediction - 60.0) * 100.0 / 60.0 should be <= 1.0
      case Failure(e) => fail(e.getMessage)
    }
  }

  "buildModel" should "build model" in {
    val experimentalData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    val m = new GeneralizedLinearRegressionMaster(sparkSession)
    val model = m.buildModel(experimentalData)
    (model != null) shouldBe true
  }

  "loadModel" should "load model" in {
    val experimentalData = LinearFunction.generate(50, Vector(10.0, 1.0, 2.0), 1.0)
    val m1 = new GeneralizedLinearRegressionMaster(sparkSession)
    val _ = m1.buildModel(experimentalData)
    m1.saveModel(modelPath) match {
      case Success(r) => r shouldBe true
      case Failure(e) => fail(e.getMessage)
    }

    val m2 = new GeneralizedLinearRegressionMaster(sparkSession)
    m2.loadModel(modelPath) match {
      case Success(_) => succeed
      case Failure(e) => fail(e.getMessage)
    }
  }


  override protected def beforeEach(): Unit = {
    super.beforeEach()
    sparkSession = SparkSession.builder
      .master("local")
      .appName("Generalized Regression Model Master test")
      .getOrCreate()
  }

  override protected def afterEach(): Unit = {
    sparkSession.close()
    super.afterEach()
  }

}
