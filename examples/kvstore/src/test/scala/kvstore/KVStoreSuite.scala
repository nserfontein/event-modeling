package kvstore

import akka.actor.ActorSystem

class KVStoreSuite
  extends Step1_PrimarySpec
    with Tools {

  implicit val system: ActorSystem = ActorSystem("KVStoreSuite")

  system.actorOf(EventHandler.props)

}
