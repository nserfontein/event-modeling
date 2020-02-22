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

  var state = State(List.empty)

  arbiter ! Join

  override def receive: Receive = {
    case PrimaryJoined =>
      context.become(leader)
  }

  val leader: Receive = {
    case Get(key, id) =>
      sender() ! GetResult(key, None, id)
    case Insert(key, value, id) =>
      emit(RecordInserted(Record(key, value)))
      context.become(waiting(pendingEventCount = 1), discardOld = false)
    case unknown =>
      log.error(s"TODO: Replica.leader $unknown")
  }

  def emit(events: Event*): Unit = {
    events.foreach(context.system.eventStream.publish)
  }

  def waiting(pendingEventCount: Int): Receive = {
    case event: Event =>
      state = state.updated(event)
      if (pendingEventCount == 1) {
        context.unbecome()
        unstashAll()
      } else {
        context.become(waiting(pendingEventCount - 1))
      }
    case _ => stash()
  }

}
