package data.playback;

import data.DataBank;
import data.DataLine;

import java.io.IOException;

/**
 * The <code>DataPopulator</code> object is used to fill a <code>DataBank</code> with data from a given file.
 * It uses as <code>FileReader</code> in order to pull data from the file. It reads each 2-byte short integer one at a time
 * and fills first a <code>DataLine</code> object with 256 shorts. It then adds the <code>DataLine</code> to the <code>DataBank</code>.
 *
 */
public class DataPopulator {

    private DataBank bank;
    private FileReader reader;
    private int lineSize = 256;

    /**
     * Creates a <code>DataPopulator</code> to fill the given <code>DataBank</code> using the given <code>FileReader</code>
     *
     * @param dataBank the databank to fill
     * @param fileReader the file reader used to fill the data bank
     */
    public DataPopulator(DataBank dataBank, FileReader fileReader) {
        bank = dataBank;
        reader = fileReader;
    }

    /**
     * Populates the data bank with the data from file.
     *
     * First reads a 2-byte short integer at a time and fills an array of size 256. Then pushes the array into a new
     * <code>DataLine</code> object into the <code>DataBank</code>.
     *
     *
     * @throws IOException If there are no more values to read
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
