package kvstore.scratch

import akka.actor.Actor

class EventLog extends Actor{

  override def receive: Receive = {
    case event: Event =>
      // log to persistent storage
      sender() ! event
  }

}
