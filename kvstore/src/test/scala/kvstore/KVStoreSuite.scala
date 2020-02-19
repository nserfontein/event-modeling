package kvstore

import akka.actor.ActorSystem

class KVStoreSuite extends Step1_PrimarySpec {

  implicit val system: ActorSystem = ActorSystem("KVStoreSuite")

}
