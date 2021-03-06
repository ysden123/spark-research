/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.spark.research.data.generator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


/**
  * @author Yuriy Stul
  */
class LinearFunctionTest extends AnyFlatSpec with Matchers {

  behavior of "LinearFunction"

  "generate" should "handle invalid parameters" in {
    assertThrows[IllegalArgumentException](
      LinearFunction.generate(0, null, 0)
    )
    assertThrows[IllegalArgumentException](
      LinearFunction.generate(1, null, 0)
    )
    assertThrows[IllegalArgumentException](
      LinearFunction.generate(1, Vector[Double](), 0)
    )
  }

  it should "generate vector (deviation is 0.0)" in {
    val numberOfSamples = 200
    val result = LinearFunction.generate(numberOfSamples, Vector(10.0, 1.0, 2.0), 0.0)
    //    result.foreach(println)
    result.length shouldBe numberOfSamples
    result.foreach(r => {
      (r._1 > 10) shouldBe true
      (r._2.sum <= (1.0 + 3.0)) shouldBe true
    })
  }

  it should "generate vector (deviation is  5.0)" in {
    val numberOfSamples = 200
    val result = LinearFunction.generate(numberOfSamples, Vector(10.0, 1.0, 2.0), 5.0)
    //    result.foreach(println)
    result.length shouldBe numberOfSamples
    result.foreach(r => {
      (r._1 > 10) shouldBe true
      (r._2.sum <= (1.0 + 3.0)) shouldBe true
    })
  }

}
