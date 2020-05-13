package rates

import actors.RunningActor
import akka.actor.{ActorSystem, Props}
import rates.struct.MSSQLCredentials
import pureconfig._
import pureconfig.generic.auto._

// TODO: explore differences in env. App runs in (1) env, but not in (2)

// (1)
// Here it runes completely fine:
// openjdk version "14.0.1" 2020-04-14
// OpenJDK Runtime Environment (build 14.0.1+7)
// OpenJDK 64-Bit Server VM (build 14.0.1+7, mixed mode, sharing)

// (2)
// Here it returnes: java.lang.ClassNotFoundException: com.bloomberglp.blpapi.impl.aT
// java version "1.8.0_251"
// Java(TM) SE Runtime Environment (build 1.8.0_251-b08)
// Java HotSpot(TM) Client VM (build 25.251-b08, mixed mode)

object SimpleCurrRatesRequestApp
  extends App {

  println("Bloomberg prices application has started!")

  val conf = ConfigSource.default.loadOrThrow[MSSQLCredentials]

  val system = ActorSystem("SchedulerSystem")
  val receiver = system.actorOf(Props(new RunningActor(conf)))
  val sched = QuartzSchedulerExtension2(system)
  val scheduleName = "weekdayAfter1pm"

  //  sched.createSchedule("everyHour", None, conf.cron) // TODO: cron exp as actor for manual input: */10 * * ? * *

  sched.schedule(scheduleName, receiver, RunningActor.Run)
  val builtSched = sched.schedules(scheduleName).buildTrigger(scheduleName)
  println(builtSched)
  sched.getFireTimeN(scheduleName) // TODO: exclude do not work or represented incorrectly

}
