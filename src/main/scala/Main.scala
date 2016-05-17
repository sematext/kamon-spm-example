package com.sematext.kamon.example

import kamon.Kamon
import akka.actor._
import akka.util.Timeout

import scala.concurrent.duration._

class Worker extends Actor with ActorLogging {
  def receive = {
    case 'work => log.info("Working")
  }
}

object Main extends App {
  Kamon.start()

  val system = ActorSystem()
  val actor = system.actorOf(Props[Worker])

  def user(): Unit = {
     actor ! 'work
     Thread.sleep(1000)
     user()
  }

  user()
}
