package pl.edu.agh.hubert.engine

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.experiments.Experiment

trait EvolutionEngine {
  def evolve()
  
  def experiment: Experiment
}

private class DefaultEvolutionEngine(val experiment: Experiment) extends EvolutionEngine {

  private val logger =  LoggerFactory.getLogger(getClass)
  
  def evolve() = {
    logger.info("Evolution iteration")
    
  }
  
}

object EvolutionEngine {
  
  def apply(experiment: Experiment): EvolutionEngine = {
    new DefaultEvolutionEngine(experiment)
  }
  
}