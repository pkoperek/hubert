package prototype.evolution.reporting;

import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPPopulation;

import java.io.BufferedWriter;
import java.io.IOException;

public class FitnessLengthFileFilePopulationReporter extends FilePopulationReporter {

    public FitnessLengthFileFilePopulationReporter() {
        super("fl");
    }

    protected void writePopulationData(GPPopulation gpPopulation, BufferedWriter writer) throws IOException {
        for (IGPProgram program : gpPopulation.getGPPrograms()) {
            writer.append(program.getFitnessValue() + "," + program.getChromosome(0).size());
            writer.newLine();
        }
    }
}