package prototype.evolution.genotype;

import java.util.List;

/**
 * User: koperek
 * Date: 23.03.13
 * Time: 16:11
 */
public class ChromosomeBuildingStrategyFactory {
    private StrategyType type;

    public enum StrategyType {
        SINGLE, PER_VARIABLE
    }

    public ChromosomeBuildingStrategyFactory(StrategyType type) {
        this.type = type;
    }

    public ChromosomeBuildingStrategy createStrategy(List<String> variableNames) {
        switch (type) {
            case SINGLE:
                return new SingleChromosomeBuildingStrategy(variableNames);
            case PER_VARIABLE:
                return new MultipleChromosomesBuildingStrategy(variableNames);
        }

        throw new IllegalArgumentException("Unsupported strategy type! " + type);
    }

}
