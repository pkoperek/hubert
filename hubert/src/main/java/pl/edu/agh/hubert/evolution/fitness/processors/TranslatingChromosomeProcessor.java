package pl.edu.agh.hubert.evolution.fitness.processors;

import org.jgap.gp.impl.ProgramChromosome;
import pl.edu.agh.hubert.data.VariablesValuesContainer;
import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNodeFactory;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNodeToFunctionTranslator;

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
