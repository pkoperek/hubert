package hubert.differentiation.symbolic;

import org.jgap.gp.CommandGene;
import org.jgap.gp.MathCommand;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import hubert.data.Pair;
import hubert.data.VariablesValuesContainer;
import hubert.differentiation.symbolic.tree.ConstantTreeNode;
import hubert.differentiation.symbolic.tree.JGAPTreeNode;
import hubert.differentiation.symbolic.tree.VariableTreeNode;

/**
 * User: koperek
 * Date: 02.03.13
 * Time: 22:09
 */
public class TreeNodeFactory {

    private final VariablesValuesContainer variablesValuesContainer;
    private final Pair<String> dependantVariables;
    private static final Pair<String> DEFAULT_DEPENDANT_VARIABLES = new Pair<String>(VariableTreeNode.EMPTY_NAME, VariableTreeNode.EMPTY_NAME);

    public TreeNodeFactory(VariablesValuesContainer variablesValuesContainer) {
        this(variablesValuesContainer, DEFAULT_DEPENDANT_VARIABLES);
    }

    public TreeNodeFactory(VariablesValuesContainer variablesValuesContainer, Pair<String> dependantVariables) {
        this.variablesValuesContainer = variablesValuesContainer;
        this.dependantVariables = dependantVariables;
    }

    public TreeNode createTreeNode(ProgramChromosome programChromosome) {
        return createTreeNode(programChromosome, 0);
    }

    public TreeNode createTreeNode(ProgramChromosome programChromosome, int geneIdx) {
        CommandGene gene = programChromosome.getGene(geneIdx);
        if (gene instanceof Variable) {
            return handleVariableGene((Variable) gene);
        }

        if (gene instanceof Terminal) {
            return handleTerminalGene((Terminal) gene);
        }

        if (gene instanceof MathCommand) {
            return handleMathCommandGene(programChromosome, geneIdx);
        }

        throw new IllegalArgumentException("Unknown gene type: " + gene.getClass());
    }

    private TreeNode handleMathCommandGene(ProgramChromosome programChromosome, int geneIdx) {
        return new JGAPTreeNode(this, programChromosome, geneIdx);
    }

    private TreeNode handleTerminalGene(Terminal gene) {
        Terminal terminal = (Terminal) gene;
        return new ConstantTreeNode(terminal.execute_float(null, 0, null));
    }

    private TreeNode handleVariableGene(Variable gene) {
        Variable variable = (Variable) gene;

        String variableName = variable.getName();
        String interdependentVariableName = VariableTreeNode.EMPTY_NAME;

        if (dependantVariables.contains(variableName)) {
            interdependentVariableName = dependantVariables.getOther(variableName);
        }

        return new VariableTreeNode(variablesValuesContainer, variableName, interdependentVariableName);
    }

}
