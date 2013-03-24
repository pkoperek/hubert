package prototype.data;

import org.apache.log4j.Logger;
import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;

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
        DataContainer dataContainer = new DataContainer(timeVariable, implicitTime);
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
        return dataContainer;
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