package kvstore

import akka.actor.{Actor, ActorRef, Props}
import kvstore.Arbiter.{Join, PrimaryJoined}

object Replica {
  def props(arbiter: ActorRef, persistenceProps: Props): Props = Props(new Replica(arbiter, persistenceProps))
}

class Replica(val arbiter: ActorRef, persistenceProps: Props) extends Actor {

  arbiter ! Join

  override def receive: Receive = {
    case PrimaryJoined =>
      context.become(leader)
  }

  val leader: Receive = {
    case _ =>
    // TODO
  }

}
