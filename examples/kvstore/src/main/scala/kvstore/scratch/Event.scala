package kvstore.scratch

sealed trait Event

case class SomeEvent1() extends Event
case class SomeEvent2() extends Event
case class SomeEvent3() extends Event
