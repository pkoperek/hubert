package pl.edu.agh.hubert.evolution.engine;

import org.jgap.gp.CommandGene;

/**
 * User: koperek
 * Date: 22.03.13
 * Time: 23:58
 */
public interface MutatedGenesFactory {
    CommandGene mutate(CommandGene toMutate);
}
