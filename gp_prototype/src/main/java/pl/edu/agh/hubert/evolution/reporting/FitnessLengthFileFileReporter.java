package pl.edu.agh.hubert.evolution.reporting;

import org.jgap.gp.IGPProgram;
import pl.edu.agh.hubert.evolution.engine.EvolutionEngineEvent;

import java.io.BufferedWriter;
import java.io.IOException;

public class FitnessLengthFileFileReporter extends FileReporter {

    public FitnessLengthFileFileReporter() {
        super("fl", 1);
    }

    protected void writeEvent(EvolutionEngineEvent event, BufferedWriter writer) throws IOException {
        for (IGPProgram program : event.getGenotype().getGPPopulation().getGPPrograms()) {
            writer.append(program.getFitnessValue() + "," + program.getChromosome(0).size());
            writer.newLine();
        }
    }
}