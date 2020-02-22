package kvstore

import akka.actor.{Actor, ActorLogging, Props}

class EventHandler extends Actor with ActorLogging {

  context.system.eventStream.subscribe(self, classOf[Event])

  override def receive: Receive = {
    case msg =>
      log.error(s"TODO: EventHandler $msg")
  }

  override def postStop(): Unit = {
    context.system.eventStream.unsubscribe(self)
  }

}

object EventHandler {
  def props: Props = Props(new EventHandler)
}
