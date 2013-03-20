package prototype.evolution.engine.dc;

import org.jgap.gp.IGPProgram;

public interface IndividualDistance {
    double distance(IGPProgram a_leftIndividual, IGPProgram a_rightIndividual);
}
