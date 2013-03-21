package prototype.evolution.engine.dc;

import org.jgap.gp.CrossMethod;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import prototype.evolution.engine.IndividualDistance;

/**
 * User: koperek
 * Date: 20.03.13
 * Time: 20:11
 * <p/>
 * Crossing Over - tu bedzie regula porownania odleglosci BranchTypingCross
 * <p/>
 * BranchTypingCross zmienia tylko 1 losowo wybrany chromosom, tworzy nowe individuale
 * mozna bez problemu porównywac to co wchodzi z tym co wychodzi
 */
@Deprecated
public class DeterministicCrowdingCross extends CrossMethod {

    private CrossMethod delegate;
    private IndividualDistance individualDistance;

    public DeterministicCrowdingCross(GPConfiguration a_configuration, IndividualDistance individualDistance) {
        this(a_configuration, a_configuration.getCrossMethod(), individualDistance);
    }

    public DeterministicCrowdingCross(GPConfiguration a_configuration) {
        this(a_configuration, a_configuration.getCrossMethod());
    }

    public DeterministicCrowdingCross(GPConfiguration a_configuration, CrossMethod delegate) {
        this(a_configuration, delegate, new HammingDistance());
    }

    public DeterministicCrowdingCross(GPConfiguration a_configuration, CrossMethod delegate, IndividualDistance individualDistance) {
        super(a_configuration);
        this.delegate = delegate;
        this.individualDistance = individualDistance;
    }

    @Override
    public IGPProgram[] operate(final IGPProgram a_i1, final IGPProgram a_i2) {
        IGPProgram[] newIndividuals = delegate.operate(a_i1, a_i2);
        IGPProgram a_c1 = newIndividuals[0];
        IGPProgram a_c2 = newIndividuals[1];

        if (individualDistance.distance(a_i1, a_c1) + individualDistance.distance(a_i2, a_c2) <
                individualDistance.distance(a_i1, a_c2) + individualDistance.distance(a_i2, a_c1)) {
            newIndividuals[0] = pickWithMaxFitness(a_i1, a_c1);
            newIndividuals[1] = pickWithMaxFitness(a_i2, a_c2);
        } else {
            newIndividuals[0] = pickWithMaxFitness(a_i1, a_c2);
            newIndividuals[1] = pickWithMaxFitness(a_i2, a_c1);
        }

        return newIndividuals;
    }

    private IGPProgram pickWithMaxFitness(IGPProgram left, IGPProgram right) {
        return left.getFitnessValue() > right.getFitnessValue() ? left : right;
    }
}
