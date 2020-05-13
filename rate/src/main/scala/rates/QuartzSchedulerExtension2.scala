package rates

import java.util.{Calendar, Date}

import akka.actor.{ExtendedActorSystem, ExtensionId, ExtensionIdProvider}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

// TODO: to create a PR into QuartzSchedulerExtentions to get Array
class QuartzSchedulerExtension2(system: ExtendedActorSystem)
  extends QuartzSchedulerExtension(system: ExtendedActorSystem) {

  def getFireTimeN(name: String, n: Int = 7): Unit = {
    import scala.collection.JavaConverters._
    val tr = for {
      jobKey <- runningJobs.get(name)
      trigger <- scheduler.getTriggersOfJob(jobKey).asScala.headOption
    } yield trigger

    val printDate = (i: Int, d: Date) => println(i + " : " + d)

    def recGetFireTime(curDate: Date, i: Int): Unit = {
      if (i == n) printDate(i, curDate) else {
        printDate(i, curDate)
        recGetFireTime(tr.get.getFireTimeAfter(curDate), i + 1)
      }
    }

    recGetFireTime(tr.get.getFireTimeAfter(Calendar.getInstance().getTime), 1)

  }
}

  object QuartzSchedulerExtension2 extends ExtensionId[QuartzSchedulerExtension2] with ExtensionIdProvider {

    override def lookup = QuartzSchedulerExtension2

    override def createExtension(system: ExtendedActorSystem): QuartzSchedulerExtension2 =
      new QuartzSchedulerExtension2(system)


  }
