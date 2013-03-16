package prototype.evolution.reporting;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPPopulation;
import prototype.evolution.engine.EvolutionEngineEvent;
import prototype.evolution.engine.EvolutionEngineEventHandler;
import prototype.evolution.engine.EvolutionEngineEventType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FilePopulationReporter implements EvolutionEngineEventHandler {
    private static Logger logger = Logger.getLogger(FilePopulationReporter.class);

    private int iteration = 0;

    private void reportPopulation(GPPopulation gpPopulation) {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(String.format("population%05d.csv", iteration)));

            for (IGPProgram program : gpPopulation.getGPPrograms()) {
                writer.append(program.getFitnessValue() + "," + program.getChromosome(0).size());
                writer.newLine();
            }

        } catch (IOException e) {
            logger.error("IO error writing population data", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("IO error writing population data", e);
                }
            }
        }

    }

    @Override
    public void handleEvolutionEngineEvent(EvolutionEngineEvent event) {
        if (logger.isTraceEnabled() && EvolutionEngineEventType.AFTER_EVOLUTION.equals(event.getType())) {
            reportPopulation(event.getGenotype());
        }
    }

    private void reportPopulation(GPGenotype genotype) {
        reportPopulation(genotype.getGPPopulation());
    }
}