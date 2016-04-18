package com.migme.sample
import scala.math._
object GradientDecent {
  val alpha = 0.3 //size of steps taken in gradient decent
  val samples = List((Vector(0.0, 0.0), 2.0), 
      (Vector(3.0, 1.0), 12.0),
      (Vector(2.0, 2.0), 18.0),
      (Vector(2.0, 1.0), 11.0))
  var thetas = new Array[Double](3)
  var sums = new Array[Double](3)
  def main(args: Array[String]) {

    for (i <- 1 to 400) {

      var cost = 0.0;
      sums=sums.map { x => x*0 }
      for (j <- 0 to (samples.size - 1)) {
        cost = h(samples(j)._1, thetas)
        sums.zipWithIndex.foreach {
          case (x, s) if (s == 0) => sums(s) = x +
            decentTerm(1, cost, samples(j)._2)
          case (x, s) if (s > 0) => sums(s) = x +
            decentTerm(samples(j)._1(s - 1), cost, samples(j)._2)
        }

      }
      thetas.zipWithIndex.foreach {
        case (theta, s) => thetas(s) =
          theta - (alpha / samples.size) * sums(s)

      }
    }
    thetas.foreach { x => println(x) }
  }

  def decentTerm(x_j: Double, cost: Double, y: Double): Double = {
    return x_j * (cost - y)
  }

  def h(x: Vector[Double], theta: Array[Double]): Double = {
    var sum: Double = 0.0
    for (i <- 0 to (x.size - 1))
      sum += x(i) * theta(i + 1);
    return sum + theta(0);
  }
}