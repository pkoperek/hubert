package prototype.evolution.engine.common;

import org.jgap.RandomGenerator;
import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.evolution.engine.MutatedGenesFactory;
import prototype.evolution.engine.Mutator;

public class SingleChangeMutator implements Mutator {

    private final MutatedGenesFactory geneMutationFactory;
    private final RandomGenerator randomGenerator;

    public SingleChangeMutator(MutatedGenesFactory geneMutationFactory, RandomGenerator randomGenerator) {
        this.geneMutationFactory = geneMutationFactory;
        this.randomGenerator = randomGenerator;
    }

    @Override
    public IGPProgram mutate(IGPProgram toMutate) {
        mutateChromosome(pickChromosomeForMutation(toMutate));
        return toMutate;
    }

    private void mutateChromosome(ProgramChromosome chromosomeToMutate) {
        int mutationIdx = pickMutationIdx(chromosomeToMutate);
        CommandGene geneToMutate = chromosomeToMutate.getGene(mutationIdx);
        chromosomeToMutate.setGene(mutationIdx, geneMutationFactory.mutate(geneToMutate));
    }

    private int pickMutationIdx(ProgramChromosome chromosomeToMutate) {
        return randomGenerator.nextInt(chromosomeToMutate.size());
    }

    private ProgramChromosome pickChromosomeForMutation(IGPProgram toMutate) {
        return toMutate.getChromosome(randomGenerator.nextInt(toMutate.size()));
    }

}

