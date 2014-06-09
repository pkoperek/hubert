package pl.edu.agh.hubert.evolution.engine.dc;

import org.jgap.FitnessFunction;
import org.jgap.RandomGenerator;
import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.util.ICloneable;
import pl.edu.agh.hubert.evolution.engine.MutatedGenesFactory;

public class AllChromosomesMutator {

    private final MutatedGenesFactory geneMutationFactory;
    private final RandomGenerator randomGenerator;

    public AllChromosomesMutator(MutatedGenesFactory geneMutationFactory, RandomGenerator randomGenerator) {
        this.geneMutationFactory = geneMutationFactory;
        this.randomGenerator = randomGenerator;
    }

    public IGPProgram mutate(IGPProgram toMutate) {
        // removing cast to ICloneable somehow makes this code unable to compile
        IGPProgram mutated = (IGPProgram) ((ICloneable) toMutate).clone();
        // clean fitness
        mutated.setFitnessValue(FitnessFunction.NO_FITNESS_VALUE);
        for (int i = 0; i < mutated.size(); i++) {
            mutateChromosome(mutated.getChromosome(i));
        }
        return mutated;
    }

    private void mutateChromosome(ProgramChromosome chromosomeToMutate) {
        int mutationIdx = pickMutationIdx(chromosomeToMutate);
        CommandGene geneToMutate = chromosomeToMutate.getGene(mutationIdx);
        chromosomeToMutate.setGene(mutationIdx, geneMutationFactory.mutate(geneToMutate));
    }

    private int pickMutationIdx(ProgramChromosome chromosomeToMutate) {
        return randomGenerator.nextInt(chromosomeToMutate.size());
    }

}

