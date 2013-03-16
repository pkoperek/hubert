package prototype.evolution.reporting;

import org.apache.log4j.Logger;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPPopulation;
import prototype.evolution.engine.EvolutionEngineEvent;
import prototype.evolution.engine.EvolutionEngineEventHandler;
import prototype.evolution.engine.EvolutionEngineEventType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 17:59
 */
public abstract class FilePopulationReporter implements EvolutionEngineEventHandler {
    private static Logger logger = Logger.getLogger(FilePopulationReporter.class);
    private int iteration = 0;
    private final String filenameFormat;
    private final int modulo;

    protected FilePopulationReporter(String filenameFormatPrefix, int modulo) {
        this.modulo = modulo;
        this.filenameFormat = filenameFormatPrefix + "_%05d.csv";
    }

    private void reportPopulation(GPPopulation gpPopulation) {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(String.format(filenameFormat, iteration)));
            writePopulationData(gpPopulation, writer);
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

    protected abstract void writePopulationData(GPPopulation gpPopulation, BufferedWriter writer) throws IOException;

    @Override
    public void handleEvolutionEngineEvent(EvolutionEngineEvent event) {
        if (EvolutionEngineEventType.AFTER_EVOLUTION.equals(event.getType())) {
            if (iteration % modulo == 0) {
                reportPopulation(event.getGenotype());
            }
            iteration++;
        }
    }

    private void reportPopulation(GPGenotype genotype) {
        reportPopulation(genotype.getGPPopulation());
    }
}
