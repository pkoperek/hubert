package prototype.differentiation.numeric;

/**
 * User: koperek
 * Date: 23.03.13
 * Time: 14:15
 */
public interface NumericalDifferentiationCalculator {
    boolean hasDifferential(String variable, int row);

    double getPartialDerivative(String differentiated, String direction, int secondRow);
}
