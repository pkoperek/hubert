package prototype.evolution.engine.dc;

import org.jgap.gp.impl.GPConfiguration;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 14:17
 */
public class DeterministicCrowdingConfiguration {

    private GPConfiguration gpConfiguration;
    private int threadsNum = DeterministicCrowdingEvolutionIteration.DEFAULT_THREADS_NUMBER;

    public GPConfiguration getGpConfiguration() {
        return gpConfiguration;
    }

    public void setGpConfiguration(GPConfiguration gpConfiguration) {
        this.gpConfiguration = gpConfiguration;
    }

    public void setThreadsNum(int threadsNum) {
        this.threadsNum = threadsNum;
    }

    public int getThreadsNum() {
        return threadsNum;
    }
}
