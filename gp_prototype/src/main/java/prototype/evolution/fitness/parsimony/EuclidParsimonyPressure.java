package prototype.evolution.fitness.parsimony;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;

/**
 * Minus tego rozwiązania (w wersji bez normalizacji) jest taki, że wysokie wartości
 * fitness i długości się anulują - więc efekt jest odwrotny od zamierzonego
 * <p/>
 * Poza tym - juz na samym początku długość dominuje w wartości funkcji fitness - więc
 * poprawianie fitness niewiele daje bo wystarczy, że rozwiązanie ma o 1 element więcej
 * i już jest chałowe
 * <p/>
 * User: koperek
 * Date: 12.03.13
 * Time: 18:52
 */
public class EuclidParsimonyPressure extends ParsimonyPressureFitnessFunction {

    private static final Logger logger = Logger.getLogger(EuclidParsimonyPressure.class);

    public EuclidParsimonyPressure(GPFitnessFunction delegateFitnessFunction) {
        super(delegateFitnessFunction);
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        double fitnessValue = getDelegateFitnessFunction().getFitnessValue(a_subject);
        double length = a_subject.getChromosome(0).size();
        double euclid = Math.sqrt(fitnessValue * fitnessValue + length * length);

        if (logger.isTraceEnabled()) {
            logger.trace("EPP: f: " + fitnessValue + " l: " + length + " D: " + euclid + " " + a_subject.getChromosome(0).toString(0));
        }

        return euclid;
    }
}
