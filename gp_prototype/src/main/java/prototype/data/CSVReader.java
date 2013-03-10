package prototype.data;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * User: koperek
 * Date: 14.02.13
 * Time: 19:43
 */
public class CSVReader {

    private BufferedReader reader;
    private String[] legend;

    public CSVReader(BufferedReader reader) {
        this.reader = reader;
    }

    public DataContainer read() throws IOException {
        readLegend();
        return readContent();
    }

    private DataContainer readContent() throws IOException {
        DataContainer dataContainer = new DataContainer(legend);
        String line;

        while ((line = reader.readLine()) != null) {
            if (isNotEmpty(line)) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    dataContainer.addValue(legend[i], Double.parseDouble(values[i]));
                }
            }
        }

        return dataContainer;
    }

    private void readLegend() throws IOException {
        String names = reader.readLine();
        if (names.startsWith("#")) {
            names = names.substring(1);
        }
        legend = names.split(",");
    }

    private boolean isNotEmpty(String line) {
        return line != null && !line.isEmpty();
    }
}
