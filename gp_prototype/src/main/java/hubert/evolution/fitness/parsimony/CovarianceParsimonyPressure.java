package hubert.evolution.fitness.parsimony;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.ProgramChromosome;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 22:07
 */
public class CovarianceParsimonyPressure implements ParsimonyPressure {

    private double ct = 0.0; // initially ignore

    public void setCt(double ct) {
        this.ct = ct;
    }

    @Override
    public void handleBeforeEvolution(GPGenotype genotype) {
        IGPProgram[] programs = genotype.getGPPopulation().getGPPrograms();
        double[] lengths = new double[programs.length];
        double[] fitness = new double[programs.length];

        double lengthsMean = 0.0;
        for (int i = 0; i < programs.length; i++) {
            lengths[i] = getProgramSize(programs[i]);
            fitness[i] = programs[i].getFitnessValue();
            lengthsMean += lengths[i];
        }

        lengthsMean /= programs.length;

        double cov = new Covariance().covariance(lengths, fitness);
        double var = new Variance().evaluate(lengths, lengthsMean);

        setCt(cov / var);
    }

    private double getProgramSize(IGPProgram program) {
        int size = 0;
        for (int i = 0; i < program.size(); i++) {
            size += program.getChromosome(i).size() / 64;
        }
        return (double) size;
    }

    @Override
    public void handleAfterEvolution(GPGenotype genotype) {
        // does nothing
    }

    @Override
    public double pressure(double fitness, ProgramChromosome a_subject) {
        double length = a_subject.size();

        // TODO - this is sometime negative!!!
        return fitness + ct * length;
    }
}
