package kvstore

case class State(records: List[Record]) {
  def updated(event: Event): State = {
    event match {
      case RecordInserted(record) =>
        copy(records = records :+ record)
    }
  }
}

case class Record(key: String, value: String)

object State {

}
