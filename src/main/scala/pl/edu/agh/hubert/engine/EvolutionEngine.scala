package pl.edu.agh.hubert.engine

import org.slf4j.LoggerFactory

trait EvolutionEngine {
  def evolve() 
}

private class DefaultEvolutionEngine extends EvolutionEngine {

  private val logger =  LoggerFactory.getLogger(getClass)
  
  def evolve() = {
    logger.info("Evolution iteration")
    
  }
  
}

object EvolutionEngine {
  
  def apply(): EvolutionEngine = {
    new DefaultEvolutionEngine
  }
  
}