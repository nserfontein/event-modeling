package kvstore.scratch

case class State(someGuard: Boolean) {

  def updated(event: Event): State = {
    event match {
      case x => ???
    }
  }
}
