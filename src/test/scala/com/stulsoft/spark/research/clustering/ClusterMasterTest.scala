package com.stulsoft.spark.research.clustering

import com.stulsoft.spark.research.data.generator.Cluster
import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import scala.util.{Failure, Success}

/**
  * @author Yuriy Stul.
  */
class ClusterMasterTest extends FlatSpec with Matchers with BeforeAndAfterEach {
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

  "buildCluster" should "build cluster model" in {
    val master = new ClusterMaster
    master.buildCluster(Cluster.generateData1())
    master.describe() match {
      case Success(descriptions) =>
        succeed
      case Failure(e) => fail(e.getMessage)
    }
  }

  "predict" should "return XYZ" in {
    val master = new ClusterMaster
    master.buildCluster(Cluster.generateData1())
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
    master.buildCluster(Cluster.generateData1())
    master.describe() match {
      case Success(descriptions) =>
        println(descriptions)
        (descriptions.length > 0) shouldBe true
        succeed
      case Failure(e) => fail(e.getMessage)
    }
  }

}
