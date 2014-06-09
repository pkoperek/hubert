package pl.edu.agh.hubert.evolution.engine.dc;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import pl.edu.agh.hubert.evolution.engine.AgeTrackedProgramChromosome;

import java.util.ArrayList;
import java.util.List;

public class AllChromosomesCrossover {

    private static final Logger logger = Logger.getLogger(AllChromosomesCrossover.class);
    private final RandomGenerator randomGenerator;
    private final int maxCrossOverDepth;
    private final GPConfiguration configuration;

    public AllChromosomesCrossover(GPConfiguration configuration) {
        this.configuration = configuration;
        this.randomGenerator = configuration.getRandomGenerator();
        this.maxCrossOverDepth = configuration.getMaxCrossoverDepth();
    }

    public IGPProgram[] cross(IGPProgram left, IGPProgram right) {
        assumeTheSameChromosomeNumber(left, right);
        IGPProgram[] crossedChildren = {createIndividual(left), createIndividual(right)};

        for (int i = 0; i < left.size(); i++) {
            ProgramChromosome[] crossedChromosomes = crossChromosome(
                    (AgeTrackedProgramChromosome) left.getChromosome(i),
                    (AgeTrackedProgramChromosome) right.getChromosome(i)
            );

            crossedChildren[0].setChromosome(i, crossedChromosomes[0]);
            crossedChildren[1].setChromosome(i, crossedChromosomes[1]);
        }

        return crossedChildren;
    }

    private GPProgram createIndividual(IGPProgram left) {
        try {
            return new GPProgram(left);
        } catch (InvalidConfigurationException e) {
            logger.error("Problems with creating new individual!", e);

            // TODO: do something smarter
            return null;
        }
    }

    private ProgramChromosome[] crossChromosome(AgeTrackedProgramChromosome leftChromosome, AgeTrackedProgramChromosome rightChromosome) {
        ProgramChromosome[] crossedChromosomes = new ProgramChromosome[2];
        int leftCrossingPoint = randomGenerator.nextInt(leftChromosome.size());
        CommandGene crossingPointGeneLeft = leftChromosome.getNode(leftCrossingPoint);

        int rightCrossingPoint = retrieveRandomMatchingGeneIdx(
                rightChromosome,
                new TypeCriterion(
                        crossingPointGeneLeft.getReturnType(),
                        crossingPointGeneLeft.getSubReturnType())
        );

        crossedChromosomes[0] = createChromosomeWithMixedGenes(leftChromosome, rightChromosome, leftCrossingPoint, rightCrossingPoint);
        crossedChromosomes[1] = createChromosomeWithMixedGenes(rightChromosome, leftChromosome, rightCrossingPoint, leftCrossingPoint);

        return crossedChromosomes;
    }

    private ProgramChromosome createChromosomeWithMixedGenes(AgeTrackedProgramChromosome parent, AgeTrackedProgramChromosome mixingParent, int parentCrossingPoint, int mixingCrossingPoint) {
        int depthParentMovedSubtree = parent.getDepth(parentCrossingPoint);
        int depthMixingMovedSubtree = mixingParent.getDepth(mixingCrossingPoint);
        int parentChromosomeMovedGenes = parent.getSize(parentCrossingPoint);
        int mixingChromosomeMoveGenes = mixingParent.getSize(mixingCrossingPoint);

        AgeTrackedProgramChromosome crossedChromosome;
        if (properDepth(depthParentMovedSubtree, depthMixingMovedSubtree)
                || hasEnoughSpace(parent, parentChromosomeMovedGenes, mixingChromosomeMoveGenes)) {

            crossedChromosome = mixingParent;
        } else {
            crossedChromosome = createSimilarChromosome(parent);

            // copy first part from parent
            System.arraycopy(parent.getFunctions(), 0, crossedChromosome.getFunctions(), 0, parentCrossingPoint);
            // insert the 'mix-in'
            System.arraycopy(mixingParent.getFunctions(), mixingCrossingPoint, crossedChromosome.getFunctions(), parentCrossingPoint, mixingChromosomeMoveGenes);
            // copy the rest of parent
            System.arraycopy(parent.getFunctions(), parentCrossingPoint + parentChromosomeMovedGenes, crossedChromosome.getFunctions(),
                    parentCrossingPoint + mixingChromosomeMoveGenes, parent.getSize(0) - parentCrossingPoint - parentChromosomeMovedGenes);

            crossedChromosome.redepth();
            crossedChromosome.setAge(Math.max(parent.getAge(), mixingParent.getAge()));
        }

        return crossedChromosome;
    }

    private AgeTrackedProgramChromosome createSimilarChromosome(ProgramChromosome leftChromosome) {
        try {
            return new AgeTrackedProgramChromosome(configuration,
                    leftChromosome.getFunctions().length,
                    leftChromosome.getFunctionSet(),
                    leftChromosome.getArgTypes(),
                    leftChromosome.getIndividual());
        } catch (InvalidConfigurationException e) {
            logger.error("Can't create new chromosome!", e);

            // TODO: something smarter
            return null;
        }
    }

    private boolean hasEnoughSpace(ProgramChromosome chromosome, int numberOfRemovedGenes, int numberOfAddedGenes) {
        return chromosome.getSize(0) - numberOfRemovedGenes + numberOfAddedGenes >= chromosome.getFunctions().length;
    }

    private boolean properDepth(int subTreeToBeRemoved, int subTreeToBeAdded) {
        return subTreeToBeRemoved - 1 + subTreeToBeAdded > maxCrossOverDepth;
    }

    private int retrieveRandomMatchingGeneIdx(ProgramChromosome rightChromosome, TypeCriterion criterion) {
        int[] matchingGenes = findMatching(
                rightChromosome,
                criterion
        );

        return matchingGenes[randomGenerator.nextInt(matchingGenes.length)];
    }


    private int[] findMatching(ProgramChromosome chromosome, TypeCriterion criterion) {
        List<Integer> matchingGenes = new ArrayList<>();
        CommandGene[] genes = chromosome.getFunctions();

        for (int i = 0; i < genes.length && genes[i] != null; i++) {
            if (criterion.match(genes[i])) {
                matchingGenes.add(i);
            }
        }

        return ArrayUtils.toPrimitive(matchingGenes.toArray(new Integer[0]));
    }

    private void assumeTheSameChromosomeNumber(IGPProgram left, IGPProgram right) {
        if (left.size() != right.size()) {
            throw new IllegalArgumentException("Programs have different chromosome numbers!");
        }
    }

    private class TypeCriterion {
        private final Class<?> type;
        private final int subType;

        public TypeCriterion(Class<?> type, int subType) {
            this.type = type;
            this.subType = subType;
        }

        public boolean match(CommandGene gene) {
            return gene.getReturnType() == type
                    && (subType == 0 || subType == gene.getSubReturnType());
        }

        @Override
        public String toString() {
            return "{" +
                    "type=" + type +
                    ", subType=" + subType +
                    '}';
        }
    }
}
