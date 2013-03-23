package prototype.evolution.engine.common;

import org.apache.log4j.Logger;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.gp.CommandGene;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import prototype.evolution.engine.MutatedGenesFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class StaticMutatedGenesFactory implements MutatedGenesFactory {

    private static final Logger logger = Logger.getLogger(StaticMutatedGenesFactory.class);
    private static final Map<Class<?>, Class[]> POSSIBLE_MUTATIONS = new HashMap<>();
    private final RandomGenerator randomGenerator;

    static {
        POSSIBLE_MUTATIONS.put(Add.class, new Class<?>[]{Subtract.class, Multiply.class, Divide.class});
        POSSIBLE_MUTATIONS.put(Subtract.class, new Class<?>[]{Add.class, Multiply.class, Divide.class});
        POSSIBLE_MUTATIONS.put(Multiply.class, new Class<?>[]{Subtract.class, Add.class, Divide.class});
        POSSIBLE_MUTATIONS.put(Divide.class, new Class<?>[]{Subtract.class, Multiply.class, Add.class});
        POSSIBLE_MUTATIONS.put(Sine.class, new Class<?>[]{Cosine.class});
        POSSIBLE_MUTATIONS.put(Cosine.class, new Class<?>[]{Sine.class});
        POSSIBLE_MUTATIONS.put(Terminal.class, new Class<?>[]{Terminal.class});
        POSSIBLE_MUTATIONS.put(Variable.class, new Class<?>[]{Variable.class});
    }

    public StaticMutatedGenesFactory(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public CommandGene mutate(CommandGene toMutate) {
        Class<?> toMutateType = toMutate.getClass();

        Class<?>[] possibleMutations = getPossibleMutations(toMutateType);
        int mutationTypeIdx = randomGenerator.nextInt(possibleMutations.length);

        return createMutation(possibleMutations[mutationTypeIdx], toMutate);
    }

    private CommandGene createMutation(Class<?> mutationType, CommandGene toMutate) {
        CommandGene mutation;

        if (Terminal.class.equals(mutationType)) {
            mutation = createTerminalMutation(toMutate);
        } else if (Variable.class.equals(mutationType)) {
            mutation = createVariableMutation(toMutate);
        } else {
            mutation = createFunctionMutation(mutationType, toMutate);
        }

        return mutation;
    }

    private CommandGene createVariableMutation(CommandGene toMutate) {
        // TODO: variable mutation
        return toMutate;
    }

    private CommandGene createTerminalMutation(CommandGene toMutate) {
        try {
            // TODO: tutaj nie powinno być hardkodowania wartości - jeden raz są już wykorzystane w AbstractChromosomeBuildingStrategy
            return new Terminal(toMutate.getGPConfiguration(), CommandGene.FloatClass, 0.0d, 10.0d, false);
        } catch (InvalidConfigurationException e) {
            logger.error("Problems creating new terminal", e);
            throw new RuntimeException("Problems creating new terminal", e);
        }
    }

    private CommandGene createFunctionMutation(Class<?> mutationType, CommandGene toMutate) {
        try {
            Constructor<CommandGene> constructor =
                    (Constructor<CommandGene>) mutationType.getConstructor(
                            GPConfiguration.class, Class.class);

            return constructor.newInstance(
                    toMutate.getGPConfiguration(),
                    CommandGene.FloatClass);
        } catch (Exception e) {
            logger.error("Problems with creating new instance!", e);
        }

        throw new RuntimeException("Problems with creating new instance!");
    }

    private Class<?>[] getPossibleMutations(Class<?> input) {
        if (!POSSIBLE_MUTATIONS.containsKey(input)) {
            throw new IllegalArgumentException("No mutations for gene: " + input);
        }

        return POSSIBLE_MUTATIONS.get(input);
    }
}