package pl.edu.agh.hubert.evolution.fitness;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 17:19
 */
public interface RawDataExpectedValueCalculator {
    boolean hasValue(int row);

    double computeExpected(String variable, int row);
}
