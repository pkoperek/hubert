package hubert.evolution.fitness.errorcalculator;

/**
 * User: koperek
 * Date: 26.03.13
 * Time: 22:50
 */
public interface ErrorCalculator {
    double calculateError(double expected, double computed);
}
