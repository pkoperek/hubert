package pl.edu.agh.hubert.evolution.fitness;

import org.jgap.gp.IGPProgram;
import pl.edu.agh.hubert.data.container.DataContainer;

/**
 * User: koperek
 * Date: 11.03.13
 * Time: 20:53
 */
class AllChromosomesMeanAbsoluteErrorFitnessFunction extends AllChromosomesAbsoluteErrorFitnessFunction {
    public AllChromosomesMeanAbsoluteErrorFitnessFunction(DataContainer dataContainer) {
        super(dataContainer);
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        double error = super.evaluate(a_subject);
        return error / getDataContainer().getRowsCount();
    }
}
