package pl.edu.agh.hubert.data.container;

import org.apache.log4j.Logger;
import pl.edu.agh.hubert.data.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataContainerFactory {

    private static final Logger logger = Logger.getLogger(DataContainerFactory.class);

    public DataContainer getDataContainer(DataContainerConfiguration configuration) throws IOException {
        DataContainer dataContainer = createDataContainer(configuration);
        fillContainerWithData(configuration.getInputFileName(), dataContainer);
        return dataContainer;
    }

    private DataContainer createDataContainer(DataContainerConfiguration configuration) {
        DataContainer dataContainer = new DefaultDataContainer(configuration.getTimeVariable());

        if (configuration.isTimedData() && configuration.isImplicitTime()) {
            dataContainer = decorateWithImplicitTime(dataContainer);
        }

        if (configuration.isMovingWindow()) {
            dataContainer = decorateWithMovingWindow(dataContainer, configuration);
        }

        return dataContainer;
    }

    private OffsetDataContainer decorateWithMovingWindow(DataContainer dataContainer, DataContainerConfiguration configuration) {
        OffsetDataContainer offsetDataContainer =
                new OffsetDataContainer(dataContainer, configuration.getMovingWindowSize());
        OffsetIncrementingThread offsetIncrementingThread =
                new OffsetIncrementingThread(offsetDataContainer, configuration.getMovingWindowInterval());

        offsetIncrementingThread.start();
        return offsetDataContainer;
    }

    private DataContainer decorateWithImplicitTime(DataContainer dataContainer) {
        return new ImplicitTimeDataContainer(dataContainer);
    }

    private void fillContainerWithData(String filename, DataContainer dataContainer) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            new CSVReader(reader).fillIn(dataContainer);
        } catch (IOException e) {
            logger.error("I/O Exception", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public DataContainer getVerificationDataContainer(DataContainerConfiguration dataContainerConfiguration) throws IOException {
        DataContainerConfiguration overriddenDataContainerConfiguration = clone(dataContainerConfiguration);
        overriddenDataContainerConfiguration.setMovingWindow(false);
        overriddenDataContainerConfiguration.setMovingWindowInterval(-1);
        overriddenDataContainerConfiguration.setMovingWindowSize(-1);
        DataContainer dataContainer = createDataContainer(overriddenDataContainerConfiguration);
        fillContainerWithData(dataContainerConfiguration.getVerificationDataInput(), dataContainer);
        return dataContainer;
    }

    private DataContainerConfiguration clone(DataContainerConfiguration dataContainerConfiguration) {
        DataContainerConfiguration containerConfiguration = new DataContainerConfiguration();

        containerConfiguration.setImplicitTime(dataContainerConfiguration.isImplicitTime());
        containerConfiguration.setInputFileName(dataContainerConfiguration.getInputFileName());
        containerConfiguration.setMovingWindow(dataContainerConfiguration.isMovingWindow());
        containerConfiguration.setMovingWindowInterval(dataContainerConfiguration.getMovingWindowInterval());
        containerConfiguration.setMovingWindowSize(dataContainerConfiguration.getMovingWindowSize());
        containerConfiguration.setTimedData(dataContainerConfiguration.isTimedData());
        containerConfiguration.setTimeVariable(dataContainerConfiguration.getTimeVariable());
        containerConfiguration.setVerificationDataInput(dataContainerConfiguration.getVerificationDataInput());

        return containerConfiguration;
    }
}