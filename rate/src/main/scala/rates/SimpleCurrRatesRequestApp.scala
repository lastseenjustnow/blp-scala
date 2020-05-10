package rates

import actors.MainRunningActor
import actors.MainRunningActor.Begin
import akka.actor.typed.ActorSystem

object SimpleCurrRatesRequestApp extends App {

  val mainRunningActor: ActorSystem[MainRunningActor.Begin] =
    ActorSystem(MainRunningActor(), "MyRunningActor")

  mainRunningActor ! Begin("GO!")

}
