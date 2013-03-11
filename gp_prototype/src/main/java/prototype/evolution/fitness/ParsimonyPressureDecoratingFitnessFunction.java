package prototype.evolution.fitness;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 00:15
 */
public class ParsimonyPressureDecoratingFitnessFunction extends GPFitnessFunction {

    private GPFitnessFunction decoratedFitnessFunction;
    private double parsimonyPressure = 0.0;

    public ParsimonyPressureDecoratingFitnessFunction(GPFitnessFunction decoratedFitnessFunction) {
        this.decoratedFitnessFunction = decoratedFitnessFunction;
    }

    public void setParsimonyPressure(double parsimonyPressure) {
        this.parsimonyPressure = parsimonyPressure;
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        return decoratedFitnessFunction.getFitnessValue(a_subject) - a_subject.getChromosome(0).size() * parsimonyPressure;
    }
}
