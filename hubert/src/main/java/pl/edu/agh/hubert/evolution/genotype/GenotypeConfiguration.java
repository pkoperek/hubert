package pl.edu.agh.hubert.evolution.genotype;

import org.apache.commons.lang.StringUtils;
import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.jgap.gp.impl.GPConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenotypeConfiguration {
    private int maxNodes = GPGenotypeBuilder.DEFAULT_MAX_NODES;
    private List<String> variablesToOmit = Collections.<String>emptyList();
    private ChromosomeBuildingStrategyFactory.StrategyType solutionChromosomes;
    private List<String> allVariableNames;
    private GPConfiguration gpConfiguration;

    public int getMaxNodes() {
        return maxNodes;
    }

    public ChromosomeBuildingStrategyFactory.StrategyType getSolutionChromosomes() {
        return solutionChromosomes;
    }

    public List<String> getVariablesToOmit() {
        return variablesToOmit;
    }

    public void setChromosomes(ChromosomeBuildingStrategyFactory.StrategyType chromosomes) {
        this.solutionChromosomes = chromosomes;
    }

    private List<String> filterVariables(String[] variables) {
        List<String> filteredVariables = new ArrayList<String>();

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

    public List<String> getAllVariableNames() {
        return allVariableNames;
    }

    public void setAllVariableNames(String[] allVariableNames) {
        this.allVariableNames = Arrays.asList(allVariableNames);
    }

    public List<String> getFiteredVariableNames() {
        return prepareVariableNames(this.allVariableNames, this.variablesToOmit);
    }

    private List<String> prepareVariableNames(List<String> variables, List<String> variablesToOmit) {
        List<String> filteredVariables = new ArrayList<>(variables);
        filteredVariables.removeAll(variablesToOmit);
        return filteredVariables;
    }

    public void setGpConfiguration(GPConfiguration gpConfiguration) {
        this.gpConfiguration = gpConfiguration;
    }

    public GPConfiguration getGpConfiguration() {
        return gpConfiguration;
    }
}