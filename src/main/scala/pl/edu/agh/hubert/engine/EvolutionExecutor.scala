package pl.edu.agh.hubert.engine


import java.util.concurrent._
import java.util.concurrent.atomic.AtomicBoolean

import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._

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
              logger.warn("Task failed: " + task, t)
            }
          }
        }
      }

      debug("Stopping")
    }

    /**
      * Assumption: There has to be a fixed limit to the number of iterations !!! It is better to track this here
      * in one place than in each engine implementation separately.
      */
    private def executeTask(task: EvolutionTask): Unit = {
      breakable {
        for (iteration <- 1 to task.iterations) {
          if (Thread.currentThread().isInterrupted) {
            debug("Breaking the loop")
            task.status = ExperimentStatus.Stopped
            break()
          }

          task.currentIteration = iteration
          if(!task.evolutionEngine.evolve()) {
            debug("Breaking the loop - objective reached")
            task.status = ExperimentStatus.FinishedSuccess
            break()
          }
        }

        task.status = ExperimentStatus.FinishedIterationLimitExceeded
      }
    }

    private def debug(text: String): Unit = {
      logger.debug("Task: " + taskNo + " " + text)
    }
  }

}

