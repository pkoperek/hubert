package prototype.data;

import org.apache.log4j.Logger;
import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import prototype.data.container.DefaultDataContainer;
import prototype.data.container.ImplicitTimeDataContainer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataContainerFactory {

    private static final Logger logger = Logger.getLogger(DataContainerFactory.class);

    private String inputFileName;
    private String timeVariable;
    private boolean implicitTime;

    public DataContainer getDataContainer() throws IOException {
        return getDataContainer(inputFileName);
    }

    private DataContainer getDataContainer(String filename) throws IOException {
        DataContainer dataContainer = createDataContainer();
        fillContainerWithData(filename, dataContainer);
        return dataContainer;
    }

    private DataContainer createDataContainer() {
        DataContainer dataContainer = new DefaultDataContainer(timeVariable);

        if (implicitTime) {
            dataContainer = decorateWithImplicitTime(dataContainer);
        }

        return dataContainer;
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

    @Configure
    public void setInputFileName(@Configuration(value = "data.file") String inputFileName) {
        this.inputFileName = inputFileName;
    }

    @Configure
    public void setTimeVariable(@Configuration(value = "data.variable.time", required = false) String timeVariable) {
        this.timeVariable = timeVariable;
    }

    @Configure
    public void setImplicitTime(@Configuration(value = "data.implicit.time", defaultValue = "false") boolean implicitTime) {
        this.implicitTime = implicitTime;
    }
}