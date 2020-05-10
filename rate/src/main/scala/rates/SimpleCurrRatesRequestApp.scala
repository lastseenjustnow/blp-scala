package rates

import actors.RunningActor
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import akka.actor.{ActorSystem, Props}
import rates.struct.MSSQLCredentials
import pureconfig._
import pureconfig.generic.auto._

object SimpleCurrRatesRequestApp extends App {

  val credentials = ConfigSource.default.loadOrThrow[MSSQLCredentials]

  val system = ActorSystem("SchedulerSystem")
  val receiver = system.actorOf(Props(new RunningActor(credentials)))
  val sched = QuartzSchedulerExtension(system)

  sched.createSchedule("everyHour", None,"*/10 * * ? * *")
  sched.schedule("everyHour", receiver, RunningActor.Run)

}
