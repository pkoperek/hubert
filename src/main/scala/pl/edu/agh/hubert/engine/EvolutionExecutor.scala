package pl.edu.agh.hubert.engine


import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent._

import org.slf4j.LoggerFactory

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
    queue.add(task)
  }
  
  def stop() = {
    running.set(false)
  }

  private class EvolutionExecutorTask(val taskNo: Int) extends Runnable {

    private val logger = LoggerFactory.getLogger(getClass)

    override def run(): Unit = {
      info("Starting")

      while (running.get()) {
        val task = queue.poll(waitTime, TimeUnit.MILLISECONDS)
        if (task != null) {
          breakable {
            for (iteration <- 1 to task.iterations) {
              if (Thread.currentThread().isInterrupted) {
                info("Breaking the loop")
                break()
              }

              task.evolutionEngine.evolve()
            }
          }
        }

        info("Stopping")
      }

    }

    private def info(text: String): Unit = {
      logger.info("Task: " + taskNo + " " + text)
    }
  }

}

