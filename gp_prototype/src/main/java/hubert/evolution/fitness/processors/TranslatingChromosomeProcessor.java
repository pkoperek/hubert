package hubert.evolution.fitness.processors;

import org.jgap.gp.impl.ProgramChromosome;
import hubert.data.VariablesValuesContainer;
import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.TreeNode;
import hubert.differentiation.symbolic.TreeNodeFactory;
import hubert.differentiation.symbolic.TreeNodeToFunctionTranslator;

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
