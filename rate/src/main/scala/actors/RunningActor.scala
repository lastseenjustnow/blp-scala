package actors

import akka.actor.Actor
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import rates.SimpleCurrRatesRequest

object RunningActor {

  final case class Run(field: String)

  def apply(): Behavior[Run] = Behaviors.receive { (context, message) =>
    val main = new SimpleCurrRatesRequest
    main.run
    Behaviors.same
  }
}

class RunningActor extends Actor {

  import RunningActor._

  override def receive: Receive = {
    case Run => {
      val main = new SimpleCurrRatesRequest
      main.run
      Behaviors.same
    }
  }
}