package com.stulsoft.spark.research.data.generator

/**
  * @author Yuriy Stul.
  */
object Cluster {
  def generateData1(): Vector[Vector[Double]] = {
    Vector(
      Vector(1, 2, 3, 4, 5),
      Vector(10.0, 3, 8, 8, 90),
      Vector(100.0, 5, 10, 80, 9),
      Vector(20.0, 6, 12, 30, 900),
      Vector(30.0, 2, 8, 10, 0.5),
      Vector(10.0, 17, 3, 20, 1.0)
    )
  }
}
