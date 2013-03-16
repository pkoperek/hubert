package prototype.evolution.fitness.parsimony;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import prototype.evolution.engine.IterationBeginHandler;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 22:14
 */
public class CovarianceParsimonyIterationBeginHandler implements IterationBeginHandler {

    private CovarianceParsimonyPressure parsimonyPressure;

    public CovarianceParsimonyIterationBeginHandler(CovarianceParsimonyPressure parsimonyPressure) {
        this.parsimonyPressure = parsimonyPressure;
    }

    @Override
    public void notifyIterationBegin(int iteration, GPGenotype genotype) {
        IGPProgram[] programs = genotype.getGPPopulation().getGPPrograms();
        double[] lengths = new double[programs.length];
        double[] fitness = new double[programs.length];

        double lengthsMean = 0.0;
        for (int i = 0; i < programs.length; i++) {
            lengths[i] = programs[i].getChromosome(0).size();
            fitness[i] = programs[i].getFitnessValue();
            lengthsMean += lengths[i];
        }

        lengthsMean /= programs.length;

        double cov = new Covariance().covariance(lengths, fitness);
        double var = new Variance().evaluate(lengths, lengthsMean);

        parsimonyPressure.setCt(cov / var);
    }
}
