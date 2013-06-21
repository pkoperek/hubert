package hubert.evolution.engine.common;

import org.jgap.RandomGenerator;
import hubert.evolution.engine.Selector;

import java.util.List;

public class RandomSelector<T> implements Selector<T> {

    private final RandomGenerator randomGenerator;

    public RandomSelector(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public T select(List<T> freeItems) {
        return freeItems.get(randomGenerator.nextInt(freeItems.size()));
    }

}
