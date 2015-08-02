package pl.edu.agh.hubert.evolution.engine.dc;

import org.jgap.RandomGenerator;

import java.util.List;

public class RandomSelector<T> {

    private final RandomGenerator randomGenerator;

    public RandomSelector(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public T select(List<T> freeItems) {
        return freeItems.get(randomGenerator.nextInt(freeItems.size()));
    }

}
