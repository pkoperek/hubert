package pl.edu.agh.hubert.evolution.engine.dc;

import org.jgap.gp.IGPProgram;
import pl.edu.agh.hubert.evolution.engine.IndividualDistance;

/**
 * User: koperek
 * Date: 21.03.13
 * Time: 18:50
 */
public class DeterministicCrowdingTournamentCondition {

    private final IndividualDistance individualDistance;

    public DeterministicCrowdingTournamentCondition(IndividualDistance individualDistance) {
        this.individualDistance = individualDistance;
    }

    private IGPProgram pickWithBestFitness(IGPProgram left, IGPProgram right) {
        return left.getFitnessValue() < right.getFitnessValue() ? left : right;
    }

    public IGPProgram[] getWinners(IGPProgram parentA, IGPProgram parentB, IGPProgram childA, IGPProgram childB) {
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
}
