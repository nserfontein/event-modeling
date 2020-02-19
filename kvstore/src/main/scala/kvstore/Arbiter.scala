package kvstore

import akka.actor.{Actor, ActorRef}

object Arbiter {
  case object Join
  case object PrimaryJoined
}

class Arbiter extends Actor {
  import Arbiter._

  var leader: Option[ActorRef] = None
  var replicas = Set.empty[ActorRef]

  override def receive: Receive = {
    case Join =>
      if (leader.isEmpty) {
        leader = Some(sender())
        replicas += sender()
        context.system.eventStream.publish(PrimaryJoined)
      }
  }

}
