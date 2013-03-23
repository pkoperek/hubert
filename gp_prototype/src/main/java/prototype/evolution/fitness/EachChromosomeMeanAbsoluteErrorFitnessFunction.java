package prototype.evolution.fitness;

import org.jgap.gp.IGPProgram;
import prototype.data.DataContainer;

/**
 * User: koperek
 * Date: 11.03.13
 * Time: 20:53
 */
class EachChromosomeMeanAbsoluteErrorFitnessFunction extends EachChromosomeAbsoluteErrorFitnessFunction {
    public EachChromosomeMeanAbsoluteErrorFitnessFunction(DataContainer dataContainer) {
        super(dataContainer);
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        double error = super.evaluate(a_subject);
        return error / getDataContainer().getRowsCount();
    }
}
