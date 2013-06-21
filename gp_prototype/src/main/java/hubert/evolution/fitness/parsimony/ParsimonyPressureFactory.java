package hubert.evolution.fitness.parsimony;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 17:28
 */
public class ParsimonyPressureFactory {

    public ParsimonyPressure createParsimonyPressure(ParsimonyPressureConfiguration configuration) {
        switch (configuration.getType()) {
            case CONSTANT:
                return new ConstantParsimonyPressure(configuration.getConstantParsimony());
            case COVARIANCE:
                return new CovarianceParsimonyPressure();
            case EUCLID:
                return new NormalizedEuclidParsimonyPressure();
        }

        return null;
    }
}
