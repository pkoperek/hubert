package hubert.evolution.fitness.errorcalculator;

/**
 * User: koperek
 * Date: 26.03.13
 * Time: 23:20
 */
public class AbsDiffSquareErrorCalculator implements ErrorCalculator {

    @Override
    public double calculateError(double expected, double computed) {
        return (expected - computed) * (expected - computed);
    }
}
