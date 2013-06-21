package hubert.evolution.fitness.processors;

import org.jgap.gp.impl.ProgramChromosome;
import hubert.data.Pair;
import hubert.data.VariablesValuesContainer;
import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.TreeNode;
import hubert.differentiation.symbolic.TreeNodeFactory;
import hubert.differentiation.symbolic.functions.Divide;

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
