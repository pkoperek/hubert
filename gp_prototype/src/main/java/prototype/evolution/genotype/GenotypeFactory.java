package prototype.evolution.genotype;

import org.apache.commons.lang.StringUtils;
import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.container.DataContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenotypeFactory {

    private int maxNodes = GPGenotypeBuilder.DEFAULT_MAX_NODES;
    private ChromosomeBuildingStrategyFactory.StrategyType solutionChromosomes;
    private List<String> variablesToOmit = Collections.<String>emptyList();

    public GPGenotype createGPGenotype(GPConfiguration configuration, DataContainer dataContainer) throws InvalidConfigurationException {
        ChromosomeBuildingStrategyFactory buildingStrategyFactory = new ChromosomeBuildingStrategyFactory(solutionChromosomes);
        List<String> variableNames = prepareVariableNames(Arrays.asList(dataContainer.getVariableNames()));
        ChromosomeBuildingStrategy buildingStrategy = buildingStrategyFactory.createStrategy(variableNames);

        return GPGenotypeBuilder
                .builder(buildingStrategy, configuration)
                .setMaxNodes(maxNodes)
                .build();
    }

    private List<String> prepareVariableNames(List<String> variables) {
        ArrayList<String> filteredVariables = new ArrayList<>(variables);
        filteredVariables.removeAll(variablesToOmit);
        return filteredVariables;
    }

    public void setChromosomes(ChromosomeBuildingStrategyFactory.StrategyType chromosomes) {
        this.solutionChromosomes = chromosomes;
    }

    private List<String> filterVariables(String[] variables) {
        List<String> filteredVariables = new ArrayList<>();

        for (int i = 0; i < variables.length; i++) {
            if (StringUtils.isNotEmpty(variables[i])) {
                filteredVariables.add(variables[i]);
            }
        }

        return filteredVariables;
    }

    @Configure
    public void setVariablesToOmit(
            @Configuration(
                    value = "genotype.omit.variables",
                    defaultValue = ""
            )
            String variablesToOmit) {
        String[] variables = variablesToOmit.split(",");
        this.variablesToOmit = filterVariables(variables);
    }

    @Configure
    public void setMaxNodes(
            @Configuration(
                    value = "genotype.max.nodes",
                    defaultValue = "" + GPGenotypeBuilder.DEFAULT_MAX_NODES
            )
            int maxNodes) {
        this.maxNodes = maxNodes;
    }

    @Configure
    public void setSolutionChromosomes(
            @Configuration(
                    value = "genotype.solutionChromosomes",
                    defaultValue = "SINGLE"
            )
            String solutionChromosomes) {
        this.solutionChromosomes = ChromosomeBuildingStrategyFactory.StrategyType.valueOf(solutionChromosomes);
    }
}