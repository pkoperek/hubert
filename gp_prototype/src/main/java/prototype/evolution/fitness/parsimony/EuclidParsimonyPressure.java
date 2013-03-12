package prototype.evolution.fitness.parsimony;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 18:52
 */
public class EuclidParsimonyPressure extends ParsimonyPressureFitnessFunction {

    public EuclidParsimonyPressure(GPFitnessFunction delegateFitnessFunction) {
        super(delegateFitnessFunction);
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        double fitnessValue = getDelegateFitnessFunction().getFitnessValue(a_subject);
        int length = a_subject.size();

        return Math.sqrt(fitnessValue * fitnessValue + length * length);
    }
}
