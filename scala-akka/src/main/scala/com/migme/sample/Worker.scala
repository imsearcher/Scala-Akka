package com.migme.sample

import scala.io.BufferedSource

import akka.actor._

case class Work(x: List[Vector[Double]],y:List[Double],thetas:Array[Double])
class Worker extends Actor {
  
  
  var sums = new Array[Double](3)
  def receive = {
    case "hello"=>
      println("alive")
    case w : Work =>
      var cost = 0.0;
      sums=sums.map { x => x*0 }
      for (j <- 0 to (w.x.size - 1)) {
        cost = GradientDecent.h(w.x(j), w.thetas)
        sums.zipWithIndex.foreach {
          case (x, s) if (s == 0) => sums(s) = x +
            GradientDecent.decentTerm(1, cost, w.y(j))
          case (x, s) if (s > 0) => sums(s) = x +
           GradientDecent.decentTerm(w.x(j)(s - 1), cost, w.y(j))
        }

      }
      sender ! Summation(sums)
  }
}