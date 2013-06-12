package prototype.evolution.engine.dc;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import prototype.evolution.engine.Tournament;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * User: koperek
 * Date: 28.03.13
 * Time: 13:41
 */
public class DeterministicCrowdingParallelTournament implements Tournament {

    private static final Logger logger = Logger.getLogger(DeterministicCrowdingParallelTournament.class);
    private final DeterministicCrowdingTournamentCondition condition = new DeterministicCrowdingTournamentCondition(new HammingDistance());
    private final ExecutorService executorService;

    public DeterministicCrowdingParallelTournament(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public IGPProgram[] getWinners(IGPProgram[] parents, IGPProgram[] children) {
        IGPProgram[] newIndividuals = new IGPProgram[parents.length];
        executeTasks(prepareComputationTasks(parents, children, newIndividuals));
        return newIndividuals;
    }

    private List<Callable<Void>> prepareComputationTasks(final IGPProgram[] parents, final IGPProgram[] children, final IGPProgram[] newIndividuals) {
        List<Callable<Void>> tasks = new LinkedList<>();

        for (int i = 0; i < parents.length; i += 2) {
            final int idx = i;
            tasks.add(new LoggedCallable() {
                @Override
                public void callLogged() {
                    IGPProgram[] winners = condition.getWinners(
                            parents[idx],
                            parents[idx + 1],
                            children[idx],
                            children[idx + 1]
                    );
                    newIndividuals[idx] = winners[0];
                    newIndividuals[idx + 1] = winners[1];
                }
            });
        }
        return tasks;
    }

    private void executeTasks(List<Callable<Void>> tasks) {
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            logger.error("Parallel execution of tournaments failed!", e);
        }
    }
}
