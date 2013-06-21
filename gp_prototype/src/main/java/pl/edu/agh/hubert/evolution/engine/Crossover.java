package pl.edu.agh.hubert.evolution.engine;

import org.jgap.gp.IGPProgram;

public interface Crossover {
    IGPProgram[] cross(IGPProgram left, IGPProgram right);
}