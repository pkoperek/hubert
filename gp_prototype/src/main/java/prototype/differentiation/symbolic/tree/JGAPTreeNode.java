package prototype.differentiation.symbolic.tree;

import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.differentiation.symbolic.CommandGeneToFunctionTypeTranslator;
import prototype.differentiation.symbolic.FunctionType;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.TreeNodeFactory;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 21:47
 */
public class JGAPTreeNode extends TreeNode {

    private static final CommandGeneToFunctionTypeTranslator functionTypeTranslator = new CommandGeneToFunctionTypeTranslator();

    private final int geneIdx;
    private final TreeNodeFactory treeNodeFactory;
    private final ProgramChromosome programChromosome;
    private final FunctionType functionType;

    public JGAPTreeNode(TreeNodeFactory treeNodeFactory, ProgramChromosome programChromosome) {
        this(treeNodeFactory, programChromosome, 0);

    }

    public JGAPTreeNode(TreeNodeFactory treeNodeFactory, ProgramChromosome programChromosome, int geneIdx) {
        this.treeNodeFactory = treeNodeFactory;
        this.programChromosome = programChromosome;
        CommandGene gene = this.programChromosome.getGene(geneIdx);
        this.functionType = functionTypeTranslator.translate(gene);
        this.geneIdx = geneIdx;
    }

    @Override
    public FunctionType getFunctionType() {
        return functionType;
    }

    @Override
    public TreeNode[] getChildren() {
        CommandGene gene = this.programChromosome.getGene(geneIdx);
        TreeNode[] children = new TreeNode[gene.size()];

        for (int i = 0; i < gene.size(); i++) {
            int childGeneIdx = programChromosome.getChild(geneIdx, i);
            children[i] = treeNodeFactory.createTreeNode(programChromosome, childGeneIdx);
        }

        return children;
    }

    public ProgramChromosome getProgramChromosome() {
        return programChromosome;
    }
}
