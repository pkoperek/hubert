package prototype.evolution.fitness.parsimony;

import org.jgap.gp.impl.ProgramChromosome;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 00:15
 */
public class ConstantParsimonyPressure implements ParsimonyPressure {

    private double constantParsimonyPressure = 0.0;

    public ConstantParsimonyPressure(double constantParsimonyPressure) {
        this.constantParsimonyPressure = constantParsimonyPressure;
    }

    @Override
    public double pressure(double fitness, ProgramChromosome a_subject) {
        // + - because we minimize this function :)
        return fitness + a_subject.size() * constantParsimonyPressure;
    }
}
