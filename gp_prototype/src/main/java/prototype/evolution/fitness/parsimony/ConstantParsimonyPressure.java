package prototype.evolution.fitness.parsimony;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 00:15
 */
public class ConstantParsimonyPressure extends ParsimonyPressureFitnessFunction {

    private double constantParsimonyPressure = 0.0;

    public ConstantParsimonyPressure(GPFitnessFunction delegateFitnessFunction, double constantParsimonyPressure) {
        super(delegateFitnessFunction);
        this.constantParsimonyPressure = constantParsimonyPressure;
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        // + - because we minimize this function :)
        return getDelegateFitnessFunction().getFitnessValue(a_subject) + a_subject.getChromosome(0).size() * constantParsimonyPressure;
    }
}
