package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object MainRunningActor {

  final case class Begin(field: String)

  def apply(): Behavior[Begin] =
    Behaviors.setup { context =>

      val runningActor = context.spawn(RunningActor(), "RunningActor")

      Behaviors.receiveMessage { message =>
        runningActor ! RunningActor.Run(message.field)
        Behaviors.same
      }
    }

}
