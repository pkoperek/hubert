package pl.edu.agh.hubert.engine


import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent._

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.experiments.ExperimentStatus

import util.control.Breaks._

import scala.collection.mutable.ArrayBuffer

class EvolutionExecutor(
                         val numberOfThreads: Int,
                         val waitTime: Int
                         ) {

  private val logger = LoggerFactory.getLogger(getClass)

  private val queue = new LinkedBlockingDeque[EvolutionTask]
  private val executorScheduler = Executors.newFixedThreadPool(numberOfThreads)
  private val futures = ArrayBuffer[Future[_]]()
  private val running = new AtomicBoolean(true)

  for (i <- 1 to numberOfThreads) {
    futures += executorScheduler.submit(new EvolutionExecutorTask(i))
  }

  logger.info("Executor initialized")

  def addTask(task: EvolutionTask) = {
    task.status = ExperimentStatus.Queued
    queue.add(task)
  }

  def stop() = {
    running.set(false)
  }

  private class EvolutionExecutorTask(val taskNo: Int) extends Runnable {

    private val logger = LoggerFactory.getLogger(getClass)

    override def run(): Unit = {
      debug("Starting")

      while (running.get()) {
        val task = queue.poll(waitTime, TimeUnit.MILLISECONDS)

        if (task != null) {
          debug("Executing: " + task.evolutionEngine.experiment.name)
          task.status = ExperimentStatus.Running
          try {
            executeTask(task)
          } catch {
            case t: Throwable => {
              task.status = ExperimentStatus.Failed
              logger.warn("Task failed: " + task)
            }
          }
        }
      }

      debug("Stopping")
    }

    private def executeTask(task: EvolutionTask): Unit = {
      breakable {
        for (iteration <- 1 to task.iterations) {
          if (Thread.currentThread().isInterrupted) {
            debug("Breaking the loop")
            task.status = ExperimentStatus.Stopped
            break()
          }

          task.currentIteration = iteration
          task.evolutionEngine.evolve()
        }

        task.status = ExperimentStatus.Finished
      }
    }

    private def debug(text: String): Unit = {
      logger.debug("Task: " + taskNo + " " + text)
    }
  }

}

