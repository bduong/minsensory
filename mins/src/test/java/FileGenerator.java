import data.DataBank;
import data.DataLine;
import data.playback.FileReader;
import data.playback.StaticDataBank;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA. User: bduong Date: 3/20/12 Time: 8:54 PM To change
 * this template use File | Settings | File Templates.
 */
public class FileGenerator {

    private static final String fileName = "random_data.bin";
    private static final int NUMBER_OF_POINTS = 10000;

//    public static void main(String [] args) throws IOException {
//        createFile();
//
//
//    }
//


    private void createFile() throws IOException {
        //ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName));
        FileOutputStream output2 = new FileOutputStream(fileName);

        Random gen = new Random();
        int num = gen.nextInt(65535*2) - 65535;

        boolean up = true;
        int array[] = new int[256];
        byte bytes[] = new byte[2];
        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {
            for (int jj = 0; jj < 256; jj++) {
                array[jj] =  num & 0x0000FFFF;
                bytes[0] = (byte)((num & 0x0000FF00) >>8);
                bytes[1] = (byte)(num & 0x000000FF);
                output2.write(bytes);
                if (up){
                    num +=6;
                }else {
                    num -=6;
                }
                if (num > 65535) {
                    up = false;
                    num -=6;
                } else if (num < 0) {
                    up = true;
                    num +=6;
                }
            }
            referenceBank.addPoint(new DataLine(array));

        }
        output2.close();
    }

    private DataBank bank;

    private DataBank referenceBank;

    @BeforeMethod
    void setup() {
        bank = new StaticDataBank();
        referenceBank = new StaticDataBank();
    }

    @Test
    public void testRead() throws IOException {
        createFile();
        FileReader reader = new FileReader(fileName);
        int array[] = new int[256];
        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {

            for (int jj = 0; jj < 256; jj++) {
                array[jj] = reader.readNextInt();
                //System.out.print(array[jj] + " ");
            }
            bank.addPoint(new DataLine(array));
        }
        reader.close();

//        int [] array1 = bank.getNextPoint().getLine();
//        int [] array2 = referenceBank.getNextPoint().getLine();
//        for (int ii = 0; ii <256 ; ii ++){
//            System.out.println(array1[ii] + " " + array2[ii] + " " + (char) array2[ii]);
//        }

        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {
            assertEquals(bank.getNextPoint(), referenceBank.getNextPoint());
        }

    }


}
