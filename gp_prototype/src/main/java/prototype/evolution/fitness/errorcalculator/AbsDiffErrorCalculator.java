package prototype.evolution.fitness.errorcalculator;

/**
 * User: koperek
 * Date: 26.03.13
 * Time: 23:07
 */
public class AbsDiffErrorCalculator implements ErrorCalculator {

    @Override
    public double calculateError(double expected, double computed) {
        return Math.abs(expected - computed);
    }
}
