package pl.edu.agh.hubert.evolution.engine;

import org.jgap.gp.IGPProgram;

public interface IndividualDistance {
    double distance(IGPProgram a_leftIndividual, IGPProgram a_rightIndividual);
}
