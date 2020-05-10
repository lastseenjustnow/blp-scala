package rates

import actors.RunningActor
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import akka.actor.{ActorSystem, Props}

object SimpleCurrRatesRequestApp extends App {

  val system = ActorSystem("SchedulerSystem")
  val receiver = system.actorOf(Props(new RunningActor))
  val sched = QuartzSchedulerExtension(system)

  sched.createSchedule("every30Seconds", None,"*/30 * * ? * *")
  sched.schedule("every30Seconds", receiver, RunningActor.Run)

}
