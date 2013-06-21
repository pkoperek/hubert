package hubert.data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: koperek
 * Date: 04.03.13
 * Time: 23:02
 */
public class PairGenerator<T> {

    public List<Pair<T>> generatePairs(List<T> elements) {
        if (elements.size() < 2) {
            throw new IllegalArgumentException("Elements list too short! Must have at least 2 elements! Has: " + elements.size());
        }

        List<Pair<T>> pairs = new ArrayList<Pair<T>>();

        for (int i = 0; i < elements.size() - 1; i++) {
            for (int j = i + 1; j < elements.size(); j++) {
                pairs.add(new Pair(elements.get(i), elements.get(j)));
            }
        }

        return pairs;
    }
}
