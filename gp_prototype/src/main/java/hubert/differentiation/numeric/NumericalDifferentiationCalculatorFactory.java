package hubert.differentiation.numeric;

import hubert.data.container.DataContainer;

/**
 * User: koperek
 * Date: 23.03.13
 * Time: 14:14
 */
public class NumericalDifferentiationCalculatorFactory {

    public enum CalculatorType {
        FORWARD, CENTRAL, BACKWARD, LOESS
    }

    public NumericalDifferentiationCalculator createCalculator(CalculatorType type, DataContainer dataContainer) {
        if (dataContainer == null) {
            throw new IllegalArgumentException("Data container can't be null!");
        }

        switch (type) {
            case FORWARD:
                return new ForwardNumericalDifferentiationCalculator(dataContainer);
            case CENTRAL:
                return new CentralNumericalDifferentiationCalculator(dataContainer);
            case BACKWARD:
                return new BackwardNumericalDifferentiationCalculator(dataContainer);
            case LOESS:
                return new LoessNumericalDifferentiationCalculator(dataContainer);
        }

        throw new IllegalArgumentException("Unsupported calculator type! " + type);
    }

}
