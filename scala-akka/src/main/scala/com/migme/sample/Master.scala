package com.migme.sample

import akka.actor.Actor
import akka.actor.Props
import akka.actor.Terminated
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic
import akka.routing.Router
import akka.event.Logging
import akka.actor.ActorRef
import akka.routing.RoundRobinPool
import akka.actor.SupervisorStrategy
import akka.actor.OneForOneStrategy
import akka.actor.ActorSelection
import akka.serialization.JSerializer
import akka.remote.serialization.ProtobufSerializer
import akka.serialization.JavaSerializer

class Master()
    extends Actor {
  val alpha = 0.3 //size of steps taken in gradient decent
  val iter = 400
  var iter_count = 0
  var thetas = new Array[Double](3)
  var sums = new Array[Double](3)
  var counter = 0
  var x: List[Vector[Double]] = List();
  var y: List[Double] = List();
  var workers = new Array[ActorSelection](4)
  for (i <- 0 to (workers.size - 1)) {
    val worker = context.actorSelection("akka.tcp://SimpleRegression@127.0.0.1:2552/user/" + s"Worker-${i}")
    workers(i) = worker;
  }
  def receive = {
    case c: Calculation =>
      x = c.x
      y = c.y
      var cost = 0.0;
      sums = sums.map { x => x * 0 }
      for (j <- 0 to (c.x.size - 1)) {
        cost = GradientDecent.h(c.x(j), thetas)
        workers(j).tell(Work(List(c.x(j)), List(c.y(j)), thetas), self);
      }

    //GradientDecent
    case s: Summation =>
      for (i <- 0 to sums.size - 1)
        sums(i) = sums(i) + s.sums(i)
      counter += 1
      iter_count = iter_count + 1
      if (iter_count == workers.size * iter) {
        println("Completed")
        thetas.foreach { x => println(x) }
      } else if (counter == workers.size) {
        counter = 0;
        thetas.zipWithIndex.foreach {
          case (theta, s) => thetas(s) =
            theta - (alpha / x.size) * sums(s)

        }

        self ! Calculation(x, y, thetas)
      }

    case _ =>
  }

}
case class Work(x: List[Vector[Double]],y:List[Double],thetas:Array[Double])
case class Calculation(x: List[Vector[Double]], y: List[Double], thetas: Array[Double])
case class Summation(sums: Array[Double])
