package kvstore.scratch

import akka.actor.{Actor, Stash}

class SomeProcessor extends Actor with Stash {

  var state: State = State(false)

  override def receive: Receive = {
    case SomeCommand() if !state.someGuard =>
      emit(SomeEvent1(), SomeEvent2())
      context.become(waitingForEvents(count = 2), discardOld = false)
    case x =>
    // TODO: send or emit done event?
  }

  def waitingForEvents(count: Int): Receive = {
    case event: Event =>
      state = state.updated(event)
      if (count == 1) {
        context.unbecome()
        unstashAll()
      } else {
        context.become(waitingForEvents(count - 1))
      }
    case _ => stash()
  }

  def emit(events: Event*): Unit = {
    // send to log and then back again (???)
    ???
  }

}
