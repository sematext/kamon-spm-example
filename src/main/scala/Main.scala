package com.sematext.kamon.example

import kamon.Kamon
import akka.actor._
import akka.util.Timeout
import kamon.context.Key
import kamon.system.SystemMetrics
import kamon.trace.SpanContext.SamplingDecision
import kamon.trace.{Span, Tracer}

import scala.concurrent.Future
import scala.concurrent.duration._
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

object Main extends App {
  Kamon.loadReportersFromConfig()
  SystemMetrics.startCollecting()

  val system = ActorSystem()
  val actor = system.actorOf(Props[Worker1], name = "testActor1")
  val actor2 = system.actorOf(Props[Worker2], name = "testActor2")

  def user(): Unit = {
    actor ! 'work
    val span = Kamon.buildSpan("trace1").start()
    val span2 = Kamon.buildSpan("trace2").start()
    span.context().createChild(span2.context().traceID, SamplingDecision.Sample)
    Thread.sleep(3000)
    span2.finish()
    span.tag("key", "value")
    Thread.sleep(3000)
    Thread.sleep(200)
    span.finish()
    val random = new Random()
    val myGauge = Kamon.gauge("my-gauge").refine("custom", "true")
    myGauge.increment(15L)

    val myHistogram = Kamon.histogram("my-histogram").refine("custom", "true")
    myHistogram.record(100L)
    myHistogram.record(10L)

    val myCounter = Kamon.counter("my-counter").refine("custom", "true")
    myCounter.increment(random.nextInt(50000))

    val myTaggedHistogram = Kamon.histogram("my-tagged-histogram").refine("custom", "true")
    myTaggedHistogram.record(50L)
    Thread.sleep(500)
    user()
  }

  user()
}
