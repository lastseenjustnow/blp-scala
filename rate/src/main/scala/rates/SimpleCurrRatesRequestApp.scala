package rates

import actors.RunningActor
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import akka.actor.{ActorSystem, Props}
import rates.struct.MSSQLCredentials

object SimpleCurrRatesRequestApp extends App {

  val console = System.console

  val credentials = MSSQLCredentials(
    scala.io.StdIn.readLine("Enter server address: "),
    scala.io.StdIn.readLine("Enter database name: "),
    scala.io.StdIn.readLine("Enter user: "),
    console.readPassword("Enter password: ").mkString
  )

  val system = ActorSystem("SchedulerSystem")
  val receiver = system.actorOf(Props(new RunningActor(credentials)))
  val sched = QuartzSchedulerExtension(system)

  sched.createSchedule("everyHour", None,"0 0 * ? * * *")
  sched.schedule("everyHour", receiver, RunningActor.Run)

}
