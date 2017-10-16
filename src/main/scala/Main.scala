package com.sematext.kamon.example

import kamon.Kamon
import akka.actor._
import kamon.trace.Tracer

import scala.concurrent.Future
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

class Worker1 extends Actor with ActorLogging {
  def receive = {
    case 'work => log.info("Working[1]")
  }
}

class Worker2 extends Actor with ActorLogging {
  def receive = {
    case 'work2 => log.info("Working[2]")
  }
}
class Worker3 extends Actor with ActorLogging {
  def receive = {
    case 'work2 => log.info("Working[3]")
  }
}

object Main extends App {
  Kamon.start()

  val system = ActorSystem()
  val actor = system.actorOf(Props[Worker1], name = "testActor1")
  val actor2 = system.actorOf(Props[Worker2], name = "testActor2")
  val actor3 = system.actorOf(Props[Worker3], name = "testActorExcluded")

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
    myGauge.record(1L)
    myGauge.record(10L)
    myGauge.record(5L)


    val myHistogram = Kamon.metrics.histogram("my-histogram")
    myHistogram.record(100L)
    myHistogram.record(10L)

    val myCounter = Kamon.metrics.counter("my-counter")
    myCounter.increment(random.nextInt(10))

    val myMMCounter = Kamon.metrics.minMaxCounter("my-mm-counter")
    myMMCounter.increment(random.nextInt(50) - 25)

    val myTaggedHistogram = Kamon.metrics.histogram("my-tagged-histogram", tags = Map("algorithm" -> "X"))
    myTaggedHistogram.record(50L)

    Tracer.withNewContext("sample-trace", autoFinish = true) {
      Future {
        "Hello Kamon"
      }.map(_.length)
        .flatMap(len => Future(len.toString))
        .map(s => Tracer.currentContext)
        .map(s => println)
    }

    Thread.sleep(500)
    user()
  }

  user()
}
