package prototype.evolution.fitness.processors;

import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.Pair;
import prototype.data.VariablesValuesContainer;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.TreeNodeFactory;
import prototype.differentiation.symbolic.functions.Divide;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 18:03
 */
public class DifferentialQuotientChromosomeProcessor implements ChromosomeProcessor<Pair<String>> {
    @Override
    public Function preprocessChromosome(VariablesValuesContainer valuesContainer, ProgramChromosome chromosome, Pair<String> context) {

        TreeNodeFactory treeNodeFactory = new TreeNodeFactory(valuesContainer, context);
        TreeNode f = treeNodeFactory.createTreeNode(chromosome);

        String x = context.getOne();
        String y = context.getTwo();

        Function dfdx = f.differentiate(x);
        Function dfdy = f.differentiate(y);

        return new Divide(dfdx, dfdy);
    }
}
