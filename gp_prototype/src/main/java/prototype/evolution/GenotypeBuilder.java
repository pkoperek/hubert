package prototype.evolution;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import prototype.data.DataContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenotypeBuilder {

    private final DataContainer dataContainer;
    private int maxNodes = GPConfigurationBuilder.DEFAULT_MAX_NODES;

    public GenotypeBuilder(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public GenotypeBuilder setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
        return this;
    }

    public static GenotypeBuilder builder(DataContainer dataContainer) {
        return new GenotypeBuilder(dataContainer);
    }

    public GPGenotype build() throws InvalidConfigurationException {
        return createGenotype(GPConfigurationBuilder.builder(dataContainer).buildConfiguration());
    }

    private List<Variable> createVariables(List<String> variableNames, GPConfiguration a_conf) throws InvalidConfigurationException {
        List<Variable> variables = new ArrayList<Variable>();

        for (String variableName : variableNames) {
            variables.add(Variable.create(a_conf, variableName, CommandGene.FloatClass));
        }

        return variables;
    }

    private GPGenotype createGenotype(GPConfiguration configuration) throws InvalidConfigurationException {
        Class[] returnTypes = createReturnTypes(dataContainer.variablesCount());
        Class[][] argTypes = createADFArgumentTypes(dataContainer.variablesCount());
        CommandGene[][] nodeSets = createNodeSets(configuration, Arrays.asList(dataContainer.getVariableNames()));

        return GPGenotype.randomInitialGenotype(configuration, returnTypes, argTypes, nodeSets, maxNodes, true);
    }

    private CommandGene[][] createNodeSets(GPConfiguration configuration, List<String> variables) throws InvalidConfigurationException {
        CommandGene[][] commandGenes = new CommandGene[variables.size()][];

        for (int i = 0; i < commandGenes.length; i++) {
            commandGenes[i] = createNodeSet(copyWithoutOne(variables, i), configuration);
        }

        return commandGenes;
    }

    private List<String> copyWithoutOne(List<String> variables, int whichToRemove) {
        List<String> copy = new ArrayList<>(variables);
        copy.remove(whichToRemove);
        return copy;
    }

    private Class[][] createADFArgumentTypes(int size) {
        Class[][] adfArgumentTypes = new Class[size][];

        for (int i = 0; i < adfArgumentTypes.length; i++) {
            adfArgumentTypes[i] = new Class[0];
        }

        return adfArgumentTypes;
    }

    private Class[] createReturnTypes(int size) {
        Class[] returnTypes = new Class[size];
        for (int i = 0; i < returnTypes.length; i++) {
            returnTypes[i] = CommandGene.FloatClass;
        }
        return returnTypes;
    }

    private CommandGene[] createNodeSet(List<String> variables, GPConfiguration configuration) throws InvalidConfigurationException {
        List<CommandGene> commandGenes = new ArrayList<CommandGene>();

        commandGenes.add(new Add(configuration, CommandGene.FloatClass));
        commandGenes.add(new Subtract(configuration, CommandGene.FloatClass));
        commandGenes.add(new Multiply(configuration, CommandGene.FloatClass));
        commandGenes.add(new Divide(configuration, CommandGene.FloatClass));
        commandGenes.add(new Terminal(configuration, CommandGene.FloatClass, 2.0d, 10.0d, false));
        commandGenes.add(new Sine(configuration, CommandGene.FloatClass));
        commandGenes.add(new Cosine(configuration, CommandGene.FloatClass));

        commandGenes.addAll(createVariables(variables, configuration));

//        genes[7] = new Exp(configuration, CommandGene.FloatClass);
//        genes[8] = new Log(configuration, CommandGene.FloatClass);
//        genes[9] = new Tangent(configuration, CommandGene.FloatClass);
//        genes[10] = new ArcCosine(configuration, CommandGene.FloatClass);
//        genes[11] = new ArcSine(configuration, CommandGene.FloatClass);
//        genes[12] = new ArcTangent(configuration, CommandGene.FloatClass);
//        genes[13] = new Pow(configuration, CommandGene.FloatClass);

        return commandGenes.toArray(new CommandGene[commandGenes.size()]);
    }
}