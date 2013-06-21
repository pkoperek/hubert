package hubert.evolution.fitness.parsimony;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.ProgramChromosome;

/**
 * /**
 * Minus tego rozwiązania (w wersji bez normalizacji) jest taki, że wysokie wartości
 * długości powodują kompletne olewanie firness
 * Poza tym - juz na samym początku długość dominuje w wartości funkcji fitness - więc
 * poprawianie fitness niewiele daje bo wystarczy, że rozwiązanie ma o 1 element więcej
 * i już jest chałowe
 * <p/>
 * User: koperek
 * Date: 12.03.13
 * Time: 18:52
 */
public class NormalizedEuclidParsimonyPressure implements ParsimonyPressure {

    private static final Logger logger = Logger.getLogger(NormalizedEuclidParsimonyPressure.class);
    private double maxLength = 1.0; // if not set - doesn't matter
    private double maxFitness = 1.0; // if not set - doesn't matter

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

    @Override
    public void handleBeforeEvolution(GPGenotype genotype) {
        double maxFitness = 0.0;
        double maxLength = 0;

        for (IGPProgram program : genotype.getGPPopulation().getGPPrograms()) {
            double fitnessValue = program.getFitnessValue();
            double length = program.getChromosome(0).size();

            if (fitnessValue > maxFitness) {
                maxFitness = fitnessValue;
            }

            if (length > maxLength) {
                maxLength = length;
            }
        }

        setMaxLength(maxLength);
        setMaxFitness(maxFitness);
    }

    @Override
    public void handleAfterEvolution(GPGenotype genotype) {
        // does nothing
    }

    @Override
    public double pressure(double fitness, ProgramChromosome a_subject) {
        double fitnessValue = fitness / maxFitness;
        double length = a_subject.size() / maxLength;
        double euclid = Math.sqrt(fitnessValue * fitnessValue + length * length);

        if (logger.isTraceEnabled()) {
            logger.trace("EPP: f: " + fitnessValue + " l: " + length + " D: " + euclid + " " + a_subject.toString(0));
        }

        return euclid;
    }
}
