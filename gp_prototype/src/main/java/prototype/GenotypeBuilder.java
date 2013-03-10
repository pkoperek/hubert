package prototype;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import prototype.data.DataContainer;

import java.util.ArrayList;
import java.util.List;

public class GenotypeBuilder {

    public static final int DEFAULT_MAX_INIT_DEPTH = 8; // 8 levels should be enough to contain 128 elements
    public static final int DEFAULT_POPULATION_SIZE = 2048; // 2048 according to article
    public static final float DEFAULT_CROSSOVER_PROBABILITY = 0.75f; // setting according to article
    public static final int DEFAULT_MAX_NODES = 128;        // 128 - maximum number of nodes in equation tree - set according to article
    public static final float DEFAULT_MUTATION_PROBABILITY = 0.01f; // setting according to article

    private int maxInitDepth = DEFAULT_MAX_INIT_DEPTH;
    private int populationSize = DEFAULT_POPULATION_SIZE;
    private float crossoverProbability = DEFAULT_CROSSOVER_PROBABILITY;
    private float mutationProbability = DEFAULT_MUTATION_PROBABILITY;
    private int maxNodes = DEFAULT_MAX_NODES;

    public GPGenotype buildGenotype(DataContainer dataContainer) throws InvalidConfigurationException {
        GPConfiguration config = new GPConfiguration();
        List<Variable> variables = createVariables(dataContainer, config);

        config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
        config.setMaxInitDepth(maxInitDepth);
        config.setPopulationSize(populationSize);
        config.setCrossoverProb(crossoverProbability);
        config.setMutationProb(mutationProbability);
        config.setFitnessFunction(new PrototypeFitnessFunction(variables, dataContainer));

        return createGenotype(config, variables);
    }

    private List<Variable> createVariables(DataContainer dataContainer, GPConfiguration a_conf) throws InvalidConfigurationException {
        List<Variable> variables = new ArrayList<Variable>();

        for (int i = 0; i < dataContainer.variablesCount(); i++) {
            String variableName = dataContainer.getVariableName(i);
            variables.add(Variable.create(a_conf, variableName, CommandGene.FloatClass));
        }

        return variables;
    }

    private GPGenotype createGenotype(GPConfiguration a_conf, List<Variable> variables) throws InvalidConfigurationException {
        Class[] returnTypes = {CommandGene.FloatClass};
        Class[][] argTypes = {{}};
        CommandGene[][] nodeSets = {
                createNodeSet(variables, a_conf)
        };

        return GPGenotype.randomInitialGenotype(a_conf, returnTypes, argTypes, nodeSets, maxNodes, true);
    }

    private CommandGene[] createNodeSet(List<Variable> variables, GPConfiguration a_conf) throws InvalidConfigurationException {
        List<CommandGene> commandGenes = new ArrayList<CommandGene>();

        commandGenes.add(new Add(a_conf, CommandGene.FloatClass));
        commandGenes.add(new Subtract(a_conf, CommandGene.FloatClass));
        commandGenes.add(new Multiply(a_conf, CommandGene.FloatClass));
        commandGenes.add(new Divide(a_conf, CommandGene.FloatClass));
        commandGenes.add(new Terminal(a_conf, CommandGene.FloatClass, 2.0d, 10.0d, false));
        commandGenes.add(new Sine(a_conf, CommandGene.FloatClass));
        commandGenes.add(new Cosine(a_conf, CommandGene.FloatClass));

        commandGenes.addAll(variables);

//        genes[7] = new Exp(a_conf, CommandGene.FloatClass);
//        genes[8] = new Log(a_conf, CommandGene.FloatClass);
//        genes[9] = new Tangent(a_conf, CommandGene.FloatClass);
//        genes[10] = new ArcCosine(a_conf, CommandGene.FloatClass);
//        genes[11] = new ArcSine(a_conf, CommandGene.FloatClass);
//        genes[12] = new ArcTangent(a_conf, CommandGene.FloatClass);
//        genes[13] = new Pow(a_conf, CommandGene.FloatClass);

        return commandGenes.toArray(new CommandGene[commandGenes.size()]);
    }
}