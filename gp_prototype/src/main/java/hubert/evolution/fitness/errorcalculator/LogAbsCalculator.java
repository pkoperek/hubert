package hubert.evolution.fitness.errorcalculator;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 16:19
 */
public class LogAbsCalculator implements ErrorCalculator {
    @Override
    public double calculateError(double expected, double computed) {
        return Math.log(1 + Math.abs(expected - computed));
    }
}
