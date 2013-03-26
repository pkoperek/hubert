package prototype.evolution.fitness.errorcalculator;

/**
 * User: koperek
 * Date: 26.03.13
 * Time: 22:53
 */
public class ErrorCalculatorFactory {

    public enum ErrorCalculatorType {
        ABSERR,
        ABSSQERR
    }

    public ErrorCalculator createErrorCalculator(ErrorCalculatorType errorCalculatorType) {
        switch (errorCalculatorType) {
            case ABSERR:
            case ABSSQERR:
        }

        throw new IllegalArgumentException("Unsupported error type! " + errorCalculatorType);
    }
}
