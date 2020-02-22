package kvstore

sealed trait Event

case class RecordInserted(record: Record) extends Event
