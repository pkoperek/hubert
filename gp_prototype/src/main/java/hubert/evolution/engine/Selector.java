package hubert.evolution.engine;

import java.util.List;

public interface Selector<T> {
    T select(List<T> freeItems);
}