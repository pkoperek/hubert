package hubert.evolution;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;

/**
 * User: koperek
 * Date: 31.03.13
 * Time: 00:15
 */
public class FitnessVerificator {

    private static final Logger logger = Logger.getLogger(FitnessVerificator.class);
    private static final String MESSAGE = "FITNESS VERIFICATION: [%3d] %.20f : %s";
    private final GPFitnessFunction fitnessFunction;

    public FitnessVerificator(GPFitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    public void verify(IGPProgram[] paretoFront) {
        logger.info("Fitness verification");
        for (int i = 0; i < paretoFront.length; i++) {
            IGPProgram paretoFrontProgram = paretoFront[i];
            if (paretoFrontProgram != null) {
                double fitnessValue = fitnessFunction.getFitnessValue(paretoFrontProgram);
                logger.info(String.format(MESSAGE, i, fitnessValue, paretoFrontProgram.toStringNorm(0)));
            }
        }
    }
}
