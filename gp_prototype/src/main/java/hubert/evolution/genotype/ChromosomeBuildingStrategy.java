package hubert.evolution.genotype;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.GPConfiguration;

/**
 * User: koperek
 * Date: 23.03.13
 * Time: 13:02
 */
public interface ChromosomeBuildingStrategy {
    CommandGene[][] createNodeSets(GPConfiguration configuration) throws InvalidConfigurationException;

    Class[] createReturnTypes();

    Class[][] createADFArgumentTypes();
}
