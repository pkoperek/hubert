package prototype.evolution.fitness.parsimony;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 22:07
 */
public class CovarianceParsimonyPressure extends ParsimonyPressureFitnessFunction {

    private double ct = 0.0; // initially ignore

    public CovarianceParsimonyPressure(GPFitnessFunction delegateFitnessFunction) {
        super(delegateFitnessFunction);
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        double fitnessValue = getDelegateFitnessFunction().getFitnessValue(a_subject);
        double length = a_subject.getChromosome(0).size();

        // TODO - this is sometime negative!!!
        return fitnessValue + ct * length;
    }

    void setCt(double ct) {
        this.ct = ct;
    }
}
