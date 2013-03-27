package prototype.evolution.engine.dc;

import org.jgap.gp.IGPProgram;
import prototype.evolution.engine.IndividualDistance;
import prototype.evolution.engine.Tournament;

/**
 * User: koperek
 * Date: 21.03.13
 * Time: 18:50
 */
public class DeterministicCrowdingTournament implements Tournament {

    private final IndividualDistance individualDistance;

    public DeterministicCrowdingTournament(IndividualDistance individualDistance) {
        this.individualDistance = individualDistance;
    }

    private IGPProgram pickWithBestFitness(IGPProgram left, IGPProgram right) {
        return left.getFitnessValue() < right.getFitnessValue() ? left : right;
    }

    private IGPProgram[] getWinners(IGPProgram parentA, IGPProgram parentB, IGPProgram childA, IGPProgram childB) {
        IGPProgram[] winners = new IGPProgram[2];
        if (individualDistance.distance(parentA, childA) + individualDistance.distance(parentB, childB) <
                individualDistance.distance(parentA, childB) + individualDistance.distance(parentB, childA)) {
            winners[0] = pickWithBestFitness(parentA, childA);
            winners[1] = pickWithBestFitness(parentB, childB);
        } else {
            winners[0] = pickWithBestFitness(parentA, childB);
            winners[1] = pickWithBestFitness(parentB, childA);
        }
        return winners;
    }

    @Override
    public IGPProgram[] getWinners(IGPProgram[] parentPairs, IGPProgram[] childrenPairs) {
        IGPProgram[] newIndividuals = new IGPProgram[parentPairs.length];

        for (int i = 0; i < parentPairs.length; i += 2) {
            IGPProgram[] winners = getWinners(
                    parentPairs[i],
                    parentPairs[i + 1],
                    childrenPairs[i],
                    childrenPairs[i + 1]
            );

            newIndividuals[i] = winners[0];
            newIndividuals[i + 1] = winners[1];
        }

        return newIndividuals;
    }
}
