package data.playback;

import data.DataBank;
import data.DataLine;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: bduong Date: 3/21/12 Time: 10:01 PM To change
 * this template use File | Settings | File Templates.
 */
public class DataPopulator {

    private DataBank bank;
    private FileReader reader;
    private int lineSize = 256;
    
    private final int NUMBER_OF_POINTS = 10000;
    
    public DataPopulator(DataBank dataBank, FileReader fileReader) {
        bank = dataBank;
        reader = fileReader;
    }

    //TODO Change 10000 hard count to length of file

    public void execute() throws IOException {
        //int count = 0;
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
