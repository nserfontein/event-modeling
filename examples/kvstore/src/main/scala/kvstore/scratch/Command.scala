package kvstore.scratch

sealed trait Command

case class SomeCommand() extends Command
