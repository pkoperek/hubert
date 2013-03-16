package prototype.evolution.fitness.parsimony;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 18:52
 */
public class NormalizedEuclidParsimonyPressure extends ParsimonyPressureFitnessFunction {

    private static final Logger logger = Logger.getLogger(NormalizedEuclidParsimonyPressure.class);
    private double maxLength = 1.0; // if not set - doesn't matter
    private double maxFitness = 1.0; // if not set - doesn't matter

    public NormalizedEuclidParsimonyPressure(GPFitnessFunction delegateFitnessFunction) {
        super(delegateFitnessFunction);
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        double fitnessValue = getDelegateFitnessFunction().getFitnessValue(a_subject) / maxFitness;
        double length = a_subject.getChromosome(0).size() / maxLength;
        double euclid = Math.sqrt(fitnessValue * fitnessValue + length * length);

        if (logger.isTraceEnabled()) {
            logger.trace("EPP: f: " + fitnessValue + " l: " + length + " D: " + euclid + " " + a_subject.getChromosome(0).toString(0));
        }

        return euclid;
    }

    public void setMaxLength(double maxLength) {
        this.maxLength = maxLength;
    }

    public double getMaxLength() {
        return maxLength;
    }

    public void setMaxFitness(double maxFitness) {
        this.maxFitness = maxFitness;
    }

    public double getMaxFitness() {
        return maxFitness;
    }
}
