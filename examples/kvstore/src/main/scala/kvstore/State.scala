package kvstore

case class State(records: List[State.Record])

object State {
  case class Record(key: String, value: String)
}
