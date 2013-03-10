package prototype;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:21
 */
public class Main {

    private static Logger logger = Logger.getLogger(Main.class);
    private static final int NUMBER_OF_INVARIANT_GENES = 7;
    private static final int ITERATIONS = 500;
    private static final int GENERATIONS_PER_ITERATION = 1;

    public static void main(String[] args) throws InvalidConfigurationException, IOException {
        DOMConfigurator.configure("log4j.xml");
        DataContainer dataContainer = getDataContainer("circle.csv");

        GPConfiguration config = new GPConfiguration();
        Variable[] variables = createVariables(dataContainer, config);

        config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
        config.setFitnessFunction(new PrototypeFitnessFunction(variables, dataContainer));
        config.setMaxInitDepth(8);
        config.setPopulationSize(2048); // 2048 according to article
        config.setCrossoverProb(0.75f); // setting according to article
        config.setMutationProb(0.01f); // setting according to article

        GPGenotype genotype = createGenotype(config, variables);

        logger.info("Starting computations");
        for (int i = 0; i < ITERATIONS; i++) {
            genotype.evolve(GENERATIONS_PER_ITERATION);
            logger.info("Iteration: " + i);
            double bestFitnessValue = genotype.getFittestProgram().getFitnessValue();
            logger.info("Best fitness: " + bestFitnessValue);
            logger.info("Fittest: " + genotype.getFittestProgram().toStringNorm(0));

            if (bestFitnessValue < 0.00001f) {
                logger.info("Found a good solution!");
                break;
            }
        }

        genotype.outputSolution(genotype.getAllTimeBest());
    }

    private static DataContainer getDataContainer(String filename) throws IOException {
        DataContainer dataContainer = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            dataContainer = new CSVReader(reader).read();
        } catch (IOException e) {
            logger.error("I/O Exception", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return dataContainer;
    }

    private static Variable[] createVariables(DataContainer dataContainer, GPConfiguration a_conf) throws InvalidConfigurationException {
        Variable[] variables = new Variable[dataContainer.variablesCount()];

        for (int i = 0; i < dataContainer.variablesCount(); i++) {
            String variableName = dataContainer.getVariableName(i);
            variables[i] = Variable.create(a_conf, variableName, CommandGene.FloatClass);
        }

        return variables;
    }

    public static GPGenotype createGenotype(GPConfiguration a_conf, Variable[] variables) throws InvalidConfigurationException {
        Class[] returnTypes = {CommandGene.FloatClass};
        Class[][] argTypes = {{}};
        CommandGene[][] nodeSets = {createNodeSet(variables, a_conf)};

        // 128 - maximum number of nodes in equation tree - set according to article
        return GPGenotype.randomInitialGenotype(a_conf, returnTypes, argTypes, nodeSets, 128, true);
    }

    private static CommandGene[] createNodeSet(Variable[] variables, GPConfiguration a_conf) throws InvalidConfigurationException {
        CommandGene[] genes = new CommandGene[variables.length + NUMBER_OF_INVARIANT_GENES];

        genes[0] = new Add(a_conf, CommandGene.FloatClass);
        genes[1] = new Subtract(a_conf, CommandGene.FloatClass);
        genes[2] = new Multiply(a_conf, CommandGene.FloatClass);
        genes[3] = new Divide(a_conf, CommandGene.FloatClass);
        genes[4] = new Terminal(a_conf, CommandGene.FloatClass, 2.0d, 10.0d, false);
        genes[5] = new Sine(a_conf, CommandGene.FloatClass);
        genes[6] = new Cosine(a_conf, CommandGene.FloatClass);
//        genes[7] = new Exp(a_conf, CommandGene.FloatClass);
//        genes[8] = new Log(a_conf, CommandGene.FloatClass);
//        genes[9] = new Tangent(a_conf, CommandGene.FloatClass);
//        genes[10] = new ArcCosine(a_conf, CommandGene.FloatClass);
//        genes[11] = new ArcSine(a_conf, CommandGene.FloatClass);
//        genes[12] = new ArcTangent(a_conf, CommandGene.FloatClass);
//        genes[13] = new Pow(a_conf, CommandGene.FloatClass);

        for (int i = 0; i < variables.length; i++) {
            genes[i + NUMBER_OF_INVARIANT_GENES] = variables[i];
        }

        return genes;
    }

}