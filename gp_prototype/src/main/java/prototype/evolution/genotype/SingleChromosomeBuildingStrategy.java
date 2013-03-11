package prototype.evolution.genotype;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.GPConfiguration;

import java.util.List;

/**
 * User: koperek
 * Date: 11.03.13
 * Time: 21:57
 */
public class SingleChromosomeBuildingStrategy extends AbstractGenotypeBuildingStrategy {

    public SingleChromosomeBuildingStrategy(List<String> variableNames) {
        super(variableNames);
    }

    @Override
    public CommandGene[][] createNodeSets(GPConfiguration configuration) throws InvalidConfigurationException {
        return new CommandGene[][]{
                createNodeSet(
                        getVariableNames(),
                        configuration
                )
        };
    }

    @Override
    public Class[] createReturnTypes() {
        return createReturnTypesOfSize(1);
    }

    @Override
    public Class[][] createADFArgumentTypes() {
        return createADFArgumentTypesOfSize(1);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
