package pl.edu.agh.hubert.experiments

object ExperimentStatus extends Enumeration {
  val Queued = Value
  val New = Value
  val Running = Value
  val Paused = Value
  val Finished = Value
  val Stopped = Value
}