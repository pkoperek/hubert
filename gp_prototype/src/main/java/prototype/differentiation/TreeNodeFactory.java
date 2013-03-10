package prototype.differentiation;

import org.jgap.gp.CommandGene;
import org.jgap.gp.MathCommand;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import prototype.Pair;
import prototype.VariablesValues;
import prototype.differentiation.tree.ConstantTreeNode;
import prototype.differentiation.tree.JGAPTreeNode;
import prototype.differentiation.tree.VariableTreeNode;

/**
 * User: koperek
 * Date: 02.03.13
 * Time: 22:09
 */
public class TreeNodeFactory {

    private final VariablesValues variablesValues;
    private final Pair<String> dependantVariables;

    public TreeNodeFactory(VariablesValues variablesValues, Pair<String> dependantVariables) {
        this.variablesValues = variablesValues;
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

        return new VariableTreeNode(variablesValues, variableName, interdependentVariableName);
    }

}
