package data.playback;

import data.DataBank;
import data.DataLine;

import java.io.IOException;

/**
 * Used to fill a <code>StaticDataBank</code> with data from a given file.
 */
public class DataPopulator {

    private DataBank bank;
    private FileReader reader;
    private int lineSize = 256;

    /**
     *
     * @param dataBank the databank to fill
     * @param fileReader the file reader used to fill the data bank
     */
    public DataPopulator(DataBank dataBank, FileReader fileReader) {
        bank = dataBank;
        reader = fileReader;
    }

    //TODO Change 10000 hard count to length of file

    /**
     * Populates the data bank with the data from file.
     *
     * @throws IOException If the file cannot be read.
     */
    public void execute() throws IOException {
        bank.clear();
        outer:
        while (true) {
            int array[] = new int[lineSize];
            for (int ii = 0; ii < lineSize; ii++) {
                try {
                    array[ii] = reader.readNextInt();
                } catch (IOException e) {
                    if (e.getMessage().equals("End of File")) {
                        break outer;
                    } else {
                        throw e;
                    }
                }
            }
            bank.addPoint(new DataLine(array));
        }
    }
}
