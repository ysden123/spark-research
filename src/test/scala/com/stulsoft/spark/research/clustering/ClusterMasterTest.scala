package com.stulsoft.spark.research.clustering

import com.stulsoft.spark.research.data.generator.Cluster
import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import scala.util.{Failure, Success}

/**
  * @author Yuriy Stul.
  */
class ClusterMasterTest extends FlatSpec with Matchers with BeforeAndAfterEach {
  val modelPath = "cluster-master-test"
  implicit var sparkSession: SparkSession = _

  override def beforeEach() {
    super.beforeEach()
    sparkSession = SparkSession.builder
      .master("local")
      .appName("Cluster Master test")
      .getOrCreate()
  }

  override def afterEach() {
    super.afterEach()
    sparkSession.close()
  }

  behavior of "ClusterMaster"

  "buildModel" should "build cluster model" in {
    val master = new ClusterMaster
    master.buildModel(Cluster.generateData1())
    master.describe() match {
      case Success(_) =>
        succeed
      case Failure(e) => fail(e.getMessage)
    }
  }

  "predict" should "return XYZ" in {
    val master = new ClusterMaster
    master.buildModel(Cluster.generateData1())
    master.predict(Vector(1, 2, 3, 4, 5)) match {
      case Success(id) => id shouldBe 0
      case Failure(e) => fail(e.getMessage)
    }
    master.predict(Vector(100.0, 5, 10, 80, 9)) match {
      case Success(id) => id shouldBe 0
      case Failure(e) => fail(e.getMessage)
    }
    master.predict(Vector(20.0, 6, 12, 30, 900)) match {
      case Success(id) => id shouldBe 1
      case Failure(e) => fail(e.getMessage)
    }
    master.predict(Vector(0.0, 6000, 12000, 0.000030, 9)) match {
      case Success(id) => id shouldBe 0
      case Failure(e) => fail(e.getMessage)
    }
  }

  "describe" should "describe cluster details" in {
    val master = new ClusterMaster
    master.buildModel(Cluster.generateData1())
    master.describe() match {
      case Success(descriptions) =>
        println(descriptions)
        descriptions.nonEmpty shouldBe true
        succeed
      case Failure(e) => fail(e.getMessage)
    }
  }

  "saveModel and loadModel" should "save and load model" in {
    val master1 = new ClusterMaster
    master1.buildModel(Cluster.generateData1())
    master1.saveModel(modelPath) match {
      case Success(_) =>
        val master2 = new ClusterMaster
        master2.loadModel(modelPath) match {
          case Success(_) =>
            master2.predict(Vector(20.0, 6, 12, 30, 900)) match {
              case Success(id) => id shouldBe 1
              case Failure(e) => fail(e.getMessage)
            }
          case Failure(e) => fail(e.getMessage)
        }
      case Failure(e) => fail(e.getMessage)
    }
  }
}
