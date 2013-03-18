package prototype.evolution.fitness.parsimony;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 22:07
 */
public class CovarianceParsimonyPressure extends ParsimonyPressureFitnessFunction implements EvolutionCycleAware {

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

    @Override
    public void handleBeforeEvolution(GPGenotype genotype) {
        IGPProgram[] programs = genotype.getGPPopulation().getGPPrograms();
        double[] lengths = new double[programs.length];
        double[] fitness = new double[programs.length];

        double lengthsMean = 0.0;
        for (int i = 0; i < programs.length; i++) {
            lengths[i] = ((double) programs[i].getChromosome(0).size()) / 128;
            fitness[i] = programs[i].getFitnessValue();
            lengthsMean += lengths[i];
        }

        lengthsMean /= programs.length;

        double cov = new Covariance().covariance(lengths, fitness);
        double var = new Variance().evaluate(lengths, lengthsMean);

        setCt(cov / var);
    }

    @Override
    public void handleAfterEvolution(GPGenotype genotype) {
        // does nothing
    }
}
