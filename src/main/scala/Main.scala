package com.sematext.kamon.example

import kamon.Kamon
import akka.actor._
import akka.util.Timeout

import scala.concurrent.duration._
import scala.util.Random

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

    //Traces and custom metrics
    val tContext = Kamon.tracer.newContext("trace1")
    Thread.sleep(400)
    val segment = tContext.startSegment("some-section-trace1", "business-logic", "kamon")
    Thread.sleep(200)
    segment.finish()
    tContext.finish()

    val tContext2 = Kamon.tracer.newContext("trace2")
    tContext2.finishWithError(new Exception("Big error"))

    val random = new Random()
    val myGauge = Kamon.metrics.gauge("my-gauge")(0L)
    myGauge.record(random.nextLong())

    val myHistogram = Kamon.metrics.histogram("my-histogram")
    myHistogram.record(random.nextLong())

    val myCounter = Kamon.metrics.counter("my-counter")
    myCounter.increment(random.nextInt(10))

    val myMMCounter = Kamon.metrics.minMaxCounter("my-mm-counter")
    myMMCounter.increment(random.nextInt(50) - 25)

    val myTaggedHistogram = Kamon.metrics.histogram("my-tagged-histogram", tags = Map("algorithm" -> "X"))
    myTaggedHistogram.record(random.nextLong())

    Thread.sleep(500)
    user()
  }

  user()
}
