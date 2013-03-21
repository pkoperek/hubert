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

    private IGPProgram pickWithMaxFitness(IGPProgram left, IGPProgram right) {
        return left.getFitnessValue() > right.getFitnessValue() ? left : right;
    }

    @Override
    public IGPProgram[] getWinners(IGPProgram parentA, IGPProgram parentB, IGPProgram childA, IGPProgram childB) {
        IGPProgram[] winners = new IGPProgram[2];
        if (individualDistance.distance(parentA, childA) + individualDistance.distance(parentB, childB) <
                individualDistance.distance(parentA, childB) + individualDistance.distance(parentB, childA)) {
            winners[0] = pickWithMaxFitness(parentA, childA);
            winners[1] = pickWithMaxFitness(parentB, childB);
        } else {
            winners[0] = pickWithMaxFitness(parentA, childB);
            winners[1] = pickWithMaxFitness(parentB, childA);
        }
        return winners;
    }
}
