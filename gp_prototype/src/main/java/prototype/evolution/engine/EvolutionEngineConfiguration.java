package prototype.evolution.engine;

import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.jgap.gp.impl.GPConfiguration;
import prototype.evolution.engine.dc.DeterministicCrowdingConfiguration;
import prototype.evolution.reporting.ParetoFrontFileReporter;

public class EvolutionEngineConfiguration {

    public enum IterationType {
        DET_CROWDING,
        REGULAR;
    }

    private int maxIterations;
    private GPConfiguration gpConfiguration;
    private double targetError;
    private boolean paretoFrontReporter = ParetoFrontFileReporter.DEFAULT_ENABLED;
    private int paretoFrontReporterFileInterval = ParetoFrontFileReporter.DEFAULT_INTERVAL;
    private int threadsNum;
    private IterationType iterationType;
    private boolean paretoLoggerReporter = false;
    private long computationTime;

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setGpConfiguration(GPConfiguration gpConfiguration) {
        this.gpConfiguration = gpConfiguration;
    }

    public GPConfiguration getGpConfiguration() {
        return gpConfiguration;
    }

    public double getTargetError() {
        return targetError;
    }

    public boolean isParetoFrontFileReporter() {
        return paretoFrontReporter;
    }

    public int getParetoFrontReporterFileInterval() {
        return paretoFrontReporterFileInterval;
    }

    public IterationType getIterationType() {
        return iterationType;
    }

    public EvolutionEngineConfiguration() {
        this.maxIterations = EvolutionEngine.DEFAULT_MAX_ITERATIONS;
        this.targetError = EvolutionEngine.DEFAULT_TARGET_ERROR;
        this.threadsNum = EvolutionIteration.DEFAULT_THREADS_NUMBER;
    }

    @Configure
    public void setThreadsNum(
            @Configuration(
                    value = "engine.threads.number",
                    defaultValue = "1"
            )
            int threadsNum) {
        this.threadsNum = threadsNum;
    }

    @Configure
    public void setMaxIterations(
            @Configuration(
                    value = "engine.max.iterations",
                    defaultValue = "" + EvolutionEngine.DEFAULT_MAX_ITERATIONS)
            int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Configure
    public void setTargetError(
            @Configuration(
                    value = "engine.target.error",
                    defaultValue = "" + EvolutionEngine.DEFAULT_TARGET_ERROR)
            double targetError) {
        this.targetError = targetError;
    }

    @Configure
    public void setParetoFrontReporter(
            @Configuration(
                    value = "engine.reporter.pareto.enabled",
                    defaultValue = "" + ParetoFrontFileReporter.DEFAULT_ENABLED)
            boolean paretoFrontReporter) {
        this.paretoFrontReporter = paretoFrontReporter;
    }

    @Configure
    public void setParetoFrontReporterFileInterval(
            @Configuration(
                    value = "engine.reporter.pareto.interval",
                    defaultValue = "" + ParetoFrontFileReporter.DEFAULT_INTERVAL)
            int paretoFrontReporterFileInterval) {
        this.paretoFrontReporterFileInterval = paretoFrontReporterFileInterval;
    }

    public void setIterationType(IterationType iterationType) {
        this.iterationType = iterationType;
    }

    @Configure
    public void setIterationType(
            @Configuration(
                    value = "engine.iteration.type",
                    defaultValue = "DET_CROWDING")
            String iterationType) {
        this.iterationType = IterationType.valueOf(iterationType.toUpperCase());
    }

    @Configure
    public void setParetoLoggerReporter(
            @Configuration(
                    value = "engine.reporter.pareto.logger",
                    defaultValue = "false"
            )
            boolean paretoLoggerReporter) {
        this.paretoLoggerReporter = paretoLoggerReporter;
    }

    public long getComputationTime() {
        return computationTime;
    }

    @Configure
    public void setComputationTime(
            @Configuration(
                    value = "engine.computation.time.limit",
                    defaultValue = "-1"
            )
            long computationTime) {
        this.computationTime = computationTime;
    }

    public boolean isParetoFrontLoggerReporter() {
        return paretoLoggerReporter;
    }

    public DeterministicCrowdingConfiguration getDeterministicCrowdingConfiguration() {
        DeterministicCrowdingConfiguration deterministicCrowdingConfiguration = new DeterministicCrowdingConfiguration();
        deterministicCrowdingConfiguration.setGpConfiguration(getGpConfiguration());
        deterministicCrowdingConfiguration.setThreadsNum(threadsNum);
        return deterministicCrowdingConfiguration;
    }
}