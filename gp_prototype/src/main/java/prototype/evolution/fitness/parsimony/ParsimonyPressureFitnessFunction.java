package prototype.evolution.fitness.parsimony;

import org.jgap.gp.GPFitnessFunction;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 18:48
 */
public abstract class ParsimonyPressureFitnessFunction extends GPFitnessFunction {

    private final GPFitnessFunction delegateFitnessFunction;

    public ParsimonyPressureFitnessFunction(GPFitnessFunction delegateFitnessFunction) {
        this.delegateFitnessFunction = delegateFitnessFunction;
    }

    protected GPFitnessFunction getDelegateFitnessFunction() {
        return delegateFitnessFunction;
    }
}
