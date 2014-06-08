package pl.edu.agh.hubert.evolution.engine.dc;

import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPProgram;
import pl.edu.agh.hubert.evolution.engine.AgeTrackedProgramChromosome;
import pl.edu.agh.hubert.evolution.engine.Tournament;

import java.util.ArrayList;
import java.util.List;

/**
 * User: koperek
 * Date: 28.03.13
 * Time: 13:56
 */
public class AgeFitnessParetoTournament implements Tournament {

    private enum Result {LEFT, RIGHT, NONE}

    private final RandomGenerator randomGenerator;

    public AgeFitnessParetoTournament(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public IGPProgram[] getWinners(IGPProgram[] parents, IGPProgram[] children) {
        List<IGPProgram> completePopulation = new ArrayList<>(parents.length + children.length);

        for (int i = 0; i < parents.length; i++) {
            completePopulation.add(parents[i]);
            completePopulation.add(children[i]);
        }

        while (completePopulation.size() > parents.length - 1) {
            IGPProgram left = select(completePopulation);
            IGPProgram right = select(completePopulation);

            switch (tournament(left, right)) {
                case LEFT:
                    completePopulation.add(left);
                    break;
                case RIGHT:
                    completePopulation.add(right);
                    break;
                default:
                    completePopulation.add(left);
                    completePopulation.add(right);
                    break;
            }
        }

        completePopulation.add(newInd(parents[0]));

        return completePopulation.toArray(new IGPProgram[parents.length]);
    }

    private GPProgram newInd(IGPProgram parent) {
        GPConfiguration a_conf = parent.getGPConfiguration();
        Class[] a_types = parent.getTypes();
        Class[][] a_argTypes = parent.getArgTypes();
        CommandGene[][] a_nodeSets = parent.getNodeSets();
        int[] a_minDepths = parent.getMinDepths();
        int[] a_maxDepths = parent.getMaxDepths();
        int a_maxNodes = parent.getMaxNodes();

        GPProgram program;
        try {
            program = new GPProgram(a_conf, a_types, a_argTypes, a_nodeSets,
                    a_minDepths, a_maxDepths, a_maxNodes);
            // TODO: ??? MAGIC NUMBERS
            program.growOrFull(2, true, a_maxNodes, new boolean[]{true}, 0);

            for (int i = 0; i < program.size(); i++) {
                try {
                    program.setChromosome(i, new AgeTrackedProgramChromosome(program.getChromosome(i)));
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();
                }
            }
            return program;
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    private Result tournament(IGPProgram left, IGPProgram right) {
        AgeTrackedProgramChromosome leftChromosome = (AgeTrackedProgramChromosome) left.getChromosome(0);
        AgeTrackedProgramChromosome rightChromosome = (AgeTrackedProgramChromosome) right.getChromosome(0);

        int leftAge = leftChromosome.getAge();
        int rightAge = rightChromosome.getAge();
        double leftFitness = left.getFitnessValue();
        double rightFitness = right.getFitnessValue();

        if (leftAge <= rightAge && leftFitness <= rightFitness) {
            return Result.LEFT;
        }

        if (rightAge <= leftAge && rightFitness <= leftFitness) {
            return Result.RIGHT;
        }

        return Result.NONE;
    }

    private IGPProgram select(List<IGPProgram> completePopulation) {
        int randomNumber = randomGenerator.nextInt(completePopulation.size());
        return completePopulation.remove(randomNumber);
    }
}
