package prototype.data;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataContainerFactory {

    private static Logger logger = Logger.getLogger(DataContainerFactory.class);

    public DataContainer getDataContainer(String filename) throws IOException {
        DataContainer dataContainer = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            dataContainer = new CSVReader(reader).read();
        } catch (IOException e) {
            logger.error("I/O Exception", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return dataContainer;
    }
}