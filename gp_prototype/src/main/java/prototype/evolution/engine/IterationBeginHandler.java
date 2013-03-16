package prototype.evolution.engine;

import org.jgap.gp.impl.GPGenotype;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 12:57
 */
public interface IterationBeginHandler {
    void notifyIterationBegin(int iteration, GPGenotype genotype);
}
