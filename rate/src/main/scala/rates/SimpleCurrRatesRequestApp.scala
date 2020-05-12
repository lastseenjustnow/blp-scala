package rates

import actors.RunningActor
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import akka.actor.{ActorSystem, Props}
import rates.struct.MSSQLCredentials
import pureconfig._
import pureconfig.generic.auto._

object SimpleCurrRatesRequestApp extends App {

  println("Bloomberg prices application has started!")

  val credentials = ConfigSource.default.loadOrThrow[MSSQLCredentials]

  val system = ActorSystem("SchedulerSystem")
  val receiver = system.actorOf(Props(new RunningActor(credentials)))
  val sched = QuartzSchedulerExtension(system)

  sched.createSchedule("everyHour", None,"0 0 * ? * * *") // TO DO: cron to configruation as actor debug: */10 * * ? * *
  sched.schedule("everyHour", receiver, RunningActor.Run)

}
