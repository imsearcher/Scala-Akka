package com.migme.sample

import java.io.File

import com.typesafe.config.ConfigFactory

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

object RegressionApp {
  val configFile = getClass.getClassLoader.
    getResource("application.conf").getFile
  val config = ConfigFactory.parseFile(new File(configFile))
  val localConfigFile = getClass.getClassLoader.
    getResource("local_application.conf").getFile
  val localConfig = ConfigFactory.parseFile(new File(localConfigFile))
  val remoteSys = ActorSystem("SimpleRegression", config)
  val localSys = ActorSystem("LocalSystem",localConfig)
  val manager = localSys.actorOf(Props[Master], "master")

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
      workers(i) = remoteSys.actorOf(Props[Worker], name = s"Worker-${i}")
    }

    manager ! Calculation(x, y, thetas);
   }
}