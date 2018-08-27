import akka.actor._
import kamon.Kamon
import kamon.system.SystemMetrics

import scala.util.Random

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
  SystemMetrics.startCollecting()
  Kamon.loadReportersFromConfig()

  val system = ActorSystem()
  val actor = system.actorOf(Props[Worker1], name = "testActor1")
  val actor2 = system.actorOf(Props[Worker2], name = "testActor2")
  val actor3 = system.actorOf(Props[Worker3], name = "testActorExcluded")



  def user(): Unit = {
    actor ! 'work

    //Traces and custom metrics

    val tContext1 = Kamon.buildSpan("trace2").start()
    tContext1.addError("Big error")
    tContext1.finish()

    val random = new Random()
    val myGauge = Kamon.gauge("my-gauge")
    myGauge.set(1L)
    myGauge.set(10L)
    myGauge.set(5L)


    val myHistogram = Kamon.histogram("my-histogram")
    myHistogram.record(100L)
    myHistogram.record(10L)

    val myCounter = Kamon.counter("my-counter")
    myCounter.increment(random.nextInt(10))

    val myMMCounter = Kamon.rangeSampler("my-mm-counter")
    myMMCounter.increment(random.nextInt(50) - 25)

    val myTaggedHistogram = Kamon.histogram("my-tagged-histogram");
    myTaggedHistogram.refine(Map("algorithm" -> "X"));
    myTaggedHistogram.record(50L)

    Thread.sleep(500)
    user()
  }

  user()
}
