/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.spark.research.data.generator

import scala.util.Random

/** Data generator for linear function
  *
  * @author Yuriy Stul
  */
object LinearFunction {
  /**
    * Generates data of a linear function
    *
    * @param numberOfSamples number of samples
    * @param coefficients    coefficients (c0,c1,...)
    * @param deviation       maximum deviation
    * @return Vector of Tuple2, where 1st item is the function value and 2nd item is collection of parameters.
    */
  def generate(numberOfSamples: Int, coefficients: Vector[Double], deviation: Double): Vector[(Double, Vector[Double])] = {
    require(numberOfSamples > 0, "numberOfSamples should be more than 0")
    require(coefficients != null, "coefficients should be defined")
    require(coefficients.length > 1, "coefficients should have length more than 0")

    (for {_ <- 1 to numberOfSamples} yield {
      val random = new Random(Random.nextInt())

      // Generate parameters
      val parameters = (for (_ <- 1 until coefficients.length) yield random.nextDouble()).toVector

      // Calculate function value
      val result = coefficients(0) + parameters
        .zipWithIndex
        .map(parameter => parameter._1 * coefficients(parameter._2 + 1) * (100.0 - deviation * random.nextDouble()) / 100.0)
        .sum

      (result, parameters)
    })
      .toVector
  }
}
