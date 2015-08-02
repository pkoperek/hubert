package pl.edu.agh.hubert.evolution.reporting;

import org.apache.log4j.Logger;
import pl.edu.agh.hubert.evolution.engine.EvolutionEngineEvent;
import pl.edu.agh.hubert.evolution.engine.EvolutionEngineEventHandler;
import pl.edu.agh.hubert.evolution.engine.EvolutionEngineEventType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 17:59
 */
public abstract class FileReporter implements EvolutionEngineEventHandler {
    private static Logger logger = Logger.getLogger(FileReporter.class);
    private int iteration = 0;
    private final String filenameFormat;
    private final int modulo;

    protected FileReporter(String filenameFormatPrefix, int modulo) {
        this.modulo = modulo;
        this.filenameFormat = filenameFormatPrefix + "_%05d.csv";
    }

    private void reportEvent(EvolutionEngineEvent event) {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(String.format(filenameFormat, iteration)));
            writeEvent(event, writer);
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

    protected abstract void writeEvent(EvolutionEngineEvent event, BufferedWriter writer) throws IOException;

    @Override
    public void handleEvolutionEngineEvent(EvolutionEngineEvent event) {
        if (EvolutionEngineEventType.AFTER_EVOLUTION.equals(event.getType())) {
            if (iteration % modulo == 0) {
                reportEvent(event);
            }
            iteration++;
        }
    }

}
