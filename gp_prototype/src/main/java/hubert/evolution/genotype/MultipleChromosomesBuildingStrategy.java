package hubert.evolution.genotype;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.GPConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * User: koperek
 * Date: 11.03.13
 * Time: 21:52
 */
class MultipleChromosomesBuildingStrategy extends AbstractChromosomeBuildingStrategy {

    public MultipleChromosomesBuildingStrategy(List<String> variableNames) {
        super(variableNames);
    }

    @Override
    public Class[] createReturnTypes() {
        return createReturnTypesOfSize(getVariableNames().size());
    }

    @Override
    public Class[][] createADFArgumentTypes() {
        return createADFArgumentTypesOfSize(getVariableNames().size());
    }

    @Override
    public CommandGene[][] createNodeSets(GPConfiguration configuration) throws InvalidConfigurationException {
        CommandGene[][] commandGenes = new CommandGene[getVariableNames().size()][];

        for (int i = 0; i < commandGenes.length; i++) {
            commandGenes[i] = createNodeSet(getVariablesWithoutIth(i), configuration);
        }

        return commandGenes;
    }

    private List<String> getVariablesWithoutIth(int whichToRemove) {
        List<String> copy = new ArrayList<>(getVariableNames());
        copy.remove(whichToRemove);
        return copy;
    }

}
