package com.migme.sample

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

object RegressionApp {

  val sys = ActorSystem("SimpleRegression")
  val manager = sys.actorOf(Props[Master], "master")

  val samples = List((Vector(0.0, 0.0), 2.0),
    (Vector(3.0, 1.0), 12.0),
    (Vector(2.0, 2.0), 18.0),
    (Vector(2.0, 1.0), 11.0))
  val x = List(Vector(0.0, 0.0),
    Vector(3.0, 1.0),
    Vector(2.0, 2.0),
    Vector(2.0, 1.0))
  val y = List(2.0, 12.0, 18.0, 11.0)
  val workers = new Array[ActorRef](samples.size)

  var thetas = new Array[Double](3)
  var sums = new Array[Double](3)
  def main(args: Array[String]) {
    for (i <- 0 to workers.size - 1) {
      workers(i) = sys.actorOf(Props[Worker], name = s"Worker-${i}")
    }

    for (i <- 1 to 1) {

      manager ! Calculation(workers, x, y, thetas);

    }
    Thread sleep 1000
    sys.terminate()

  }
}