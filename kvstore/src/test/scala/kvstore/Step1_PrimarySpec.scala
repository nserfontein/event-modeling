package kvstore

import akka.testkit.TestProbe
import org.junit.Test

trait Step1_PrimarySpec {
  this: KVStoreSuite =>

  import Arbiter._

  @Test
  def `Step1-case1: Primary (in isolation) should properly register itself to the provided Arbiter`(): Unit = {
    val arbiter = TestProbe()
    system.actorOf(Replica.props(arbiter.ref, Persistence.props(flaky = false)), name = "step1-case1-primary")

    arbiter.expectMsg(Join)
    ()
  }

}
