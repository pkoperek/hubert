package prototype.evolution.fitness.processors;

import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.VariablesValuesContainer;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.TreeNodeFactory;
import prototype.differentiation.symbolic.TreeNodeToFunctionTranslator;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 18:08
 */
public class TranslatingChromosomeProcessor implements ChromosomeProcessor {
    @Override
    public Function preprocessChromosome(VariablesValuesContainer valuesContainer, ProgramChromosome chromosome, Object unused) {
        TreeNode chromosomeAsTree = new TreeNodeFactory(valuesContainer).createTreeNode(chromosome);
        return new TreeNodeToFunctionTranslator().translate(chromosomeAsTree);
    }
}
