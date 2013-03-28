package prototype.evolution.genotype;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractChromosomeBuildingStrategy implements ChromosomeBuildingStrategy {

    private final List<String> variableNames;

    public AbstractChromosomeBuildingStrategy(List<String> variableNames) {
        this.variableNames = variableNames;
    }

    protected Class[][] createADFArgumentTypesOfSize(int size) {
        Class[][] adfArgumentTypes = new Class[size][];

        for (int i = 0; i < adfArgumentTypes.length; i++) {
            adfArgumentTypes[i] = new Class[0];
        }

        return adfArgumentTypes;
    }

    protected Class[] createReturnTypesOfSize(int size) {
        Class[] returnTypes = new Class[size];
        for (int i = 0; i < returnTypes.length; i++) {
            returnTypes[i] = CommandGene.FloatClass;
        }
        return returnTypes;
    }

    protected CommandGene[] createNodeSet(List<String> variables, GPConfiguration configuration) throws InvalidConfigurationException {
        List<CommandGene> commandGenes = new ArrayList<CommandGene>();

        commandGenes.add(new Add(configuration, CommandGene.FloatClass));
        commandGenes.add(new Subtract(configuration, CommandGene.FloatClass));
        commandGenes.add(new Multiply(configuration, CommandGene.FloatClass));
        commandGenes.add(new Divide(configuration, CommandGene.FloatClass));
        commandGenes.add(new Terminal(configuration, CommandGene.FloatClass, 0.0d, 10.0d, false));
        commandGenes.add(new Sine(configuration, CommandGene.FloatClass));
        commandGenes.add(new Cosine(configuration, CommandGene.FloatClass));

        commandGenes.addAll(createJGAPVariables(variables, configuration));

//        genes[7] = new Exp(configuration, CommandGene.FloatClass);
//        genes[8] = new Log(configuration, CommandGene.FloatClass);
//        genes[9] = new Tangent(configuration, CommandGene.FloatClass);
//        genes[10] = new ArcCosine(configuration, CommandGene.FloatClass);
//        genes[11] = new ArcSine(configuration, CommandGene.FloatClass);
//        genes[12] = new ArcTangent(configuration, CommandGene.FloatClass);
//        genes[13] = new Pow(configuration, CommandGene.FloatClass);

        return commandGenes.toArray(new CommandGene[commandGenes.size()]);
    }

    private List<Variable> createJGAPVariables(List<String> variableNames, GPConfiguration a_conf) throws InvalidConfigurationException {
        List<Variable> variables = new ArrayList<>();

        for (String variableName : variableNames) {
            variables.add(Variable.create(a_conf, variableName, CommandGene.FloatClass));
        }

        return variables;
    }

    protected List<String> getVariableNames() {
        return variableNames;
    }
}