package actors

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
