package hubert.evolution.fitness.errorcalculator;

/**
 * User: koperek
 * Date: 26.03.13
 * Time: 22:53
 */
public class ErrorCalculatorFactory {

    public enum ErrorCalculatorType {
        ABSERR,
        ABSSQERR,
        LOGABSERR
    }

    public ErrorCalculator createErrorCalculator(ErrorCalculatorType errorCalculatorType) {
        switch (errorCalculatorType) {
            case ABSERR:
                return new AbsDiffErrorCalculator();
            case ABSSQERR:
                return new AbsDiffSquareErrorCalculator();
            case LOGABSERR:
                return new LogAbsCalculator();
        }

        throw new IllegalArgumentException("Unsupported error type! " + errorCalculatorType);
    }
}
