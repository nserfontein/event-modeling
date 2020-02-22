package kvstore

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Stash}
import kvstore.Arbiter.{Join, PrimaryJoined}

object Replica {

  sealed trait Operation {

    def key: String

    def id: Long

  }

  sealed trait OperationReply

  case class Get(key: String, id: Long) extends Operation

  case class GetResult(key: String, valueOption: Option[String], id: Long) extends OperationReply

  case class Insert(key: String, value: String, id: Long) extends Operation

  case class OperationAck(id: Long) extends OperationReply

  case class OperationFailed(id: Long) extends OperationReply

  case class Remove(key: String, id: Long) extends Operation

  def props(arbiter: ActorRef, persistenceProps: Props): Props = Props(new Replica(arbiter, persistenceProps))

}

class Replica(val arbiter: ActorRef, persistenceProps: Props) extends Actor with Stash with ActorLogging {

  import Replica._

  arbiter ! Join

  override def receive: Receive = {
    case PrimaryJoined =>
      context.become(leader)
  }

  val leader: Receive = {
    case Get(key, id) =>
      sender() ! GetResult(key, None, id)
    case Insert(key, value, id) =>

    case unknown =>
      log.error(s"TODO: Replica.leader $unknown")
  }

  def waiting(pendingEventCount: Int): Receive = {
    case event: Event => ???
  }

}
