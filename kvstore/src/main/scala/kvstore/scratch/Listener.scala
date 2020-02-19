package kvstore.scratch

import akka.actor.Actor

class Listener extends Actor{

  context.system.eventStream.subscribe(self, classOf[Event])

  override def receive: Receive = {
    case e: Event =>
      ???
  }

  override def postStop(): Unit = {
    context.system.eventStream.unsubscribe(self)
  }

}
