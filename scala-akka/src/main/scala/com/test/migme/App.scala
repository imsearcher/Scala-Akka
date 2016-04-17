package com.test.migme

import akka.actor._
import akka.routing.RandomGroup
import scala.concurrent.duration.Duration
import scala.concurrent.duration
/*
 * @author ${user.name}
 */
object App {

  def foo(x: Array[String]) = x.foldLeft("")((a, b) => a + b)
  val a = "Hello"
  val arr = new Array[Int](5);
  class HelloActor extends Actor {
    def receive = {
      case "hello" => println("hello back at you")
      case _       => println("what the hell?")
    }
  }

  def main(args: Array[String]) {

    //    for(value<-1 to arr.length)
    //         println( "int"+value )
    //    println( "Hello World!" )
    //    println("concat arguments = " + foo(args)+a)
    val system = ActorSystem("HelloSystem")
    // default Actor constructor
    val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
    val helloActor2 = system.actorOf(Props[HelloActor], name = "helloactor2")
    helloActor ! "hello"
    helloActor2 ! "hello"
    helloActor ! " "
  }

}
