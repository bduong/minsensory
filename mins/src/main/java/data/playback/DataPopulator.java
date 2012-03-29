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
    
    public DataPopulator(DataBank dataBank, FileReader fileReader) {
        bank = dataBank;
        reader = fileReader;
    }

    //TODO Change 10000 hard count to length of file

    public void execute(){
        int array[] = new int[lineSize];
        int count = 0;
        outer:
        while (count < 10000) {

            for (int ii = 0; ii < lineSize; ii++) {
                try {
                    array[ii] = reader.readNextInt();
                } catch (IOException e) {
                    break outer;
                }
            }
            count++;
            bank.addPoint(new DataLine(array));
        }
    }


}