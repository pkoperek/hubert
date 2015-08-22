package pl.edu.agh.hubert.engine

trait EvolutionEngine {
  def evolve() 
}

private class DefaultEvolutionEngine extends EvolutionEngine {
  
  def evolve() = {
    
    
  }
  
}

object EvolutionEngine {
  
  def apply(): EvolutionEngine = {
    new DefaultEvolutionEngine
  }
  
}