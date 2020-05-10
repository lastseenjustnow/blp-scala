package actors

import akka.actor.Actor
import akka.actor.typed.scaladsl.Behaviors
import rates.SimpleCurrRatesRequest
import rates.struct.MSSQLCredentials

object RunningActor {
  final case class Run(field: String)
}

class RunningActor(cred: MSSQLCredentials) extends Actor {

  import RunningActor._

  override def receive: Receive = {
    case Run => {
      val main = new SimpleCurrRatesRequest
      main.run(cred)
      Behaviors.same
    }
  }
}