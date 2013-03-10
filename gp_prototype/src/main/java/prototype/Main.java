package prototype;

import org.apache.log4j.xml.DOMConfigurator;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.DataContainer;
import prototype.data.DataContainerFactory;

import java.io.IOException;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:21
 */
public class Main {

    private static final int ITERATIONS = 500;
    private static final int GENERATIONS_PER_ITERATION = 1;

    public static void main(String[] args) throws InvalidConfigurationException, IOException {
        DOMConfigurator.configure("log4j.xml");

        DataContainer dataContainer = new DataContainerFactory().getDataContainer(args[0]);
        GPGenotype genotype = new GenotypeBuilder().buildGenotype(dataContainer);
        new GenotypeEvolutionEngine(ITERATIONS, GENERATIONS_PER_ITERATION).genotypeEvolve(genotype);
    }

}