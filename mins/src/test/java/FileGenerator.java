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

    private void createDatFile() throws IOException    {
        FileOutputStream output2 = new FileOutputStream(fileName);

        Random gen = new Random();

        int array[] = new int[256];
        for (int jj = 0; jj < 256; jj++) {
            array[jj] = 1000* (jj % 10) ;
        }
        boolean [] up = new boolean[256];
        for (int jj = 0; jj < 256; jj++) {
            up[jj] = true;
        }
        byte bytes[] = new byte[2];
        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {
            for (int jj = 0; jj < 256; jj++) {
                int num = array[jj] & 0x0000FFFF;
                bytes[0] = (byte)((num & 0x0000FF00) >>8);
                bytes[1] = (byte)(num & 0x000000FF);
                output2.write(bytes, 0, 2);
                if (up[jj]){
                    array[jj] +=15563;
                }else {
                    array[jj] -=26325;
                }
                if (array[jj] > 65535) {
                    up[jj] = false;
                    array[jj] -=17885;
                } else if (array[jj] < 0) {
                    up[jj] = true;
                    array[jj] +=30254;
                }
            }
            System.out.println(array[0] & 0x000003FF);
        }
        output2.close();
    }

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
                    num +=33;
                }else {
                    num -=33;
                }
                if (num > 65535) {
                    up = false;
                    num -=58;
                } else if (num < 0) {
                    up = true;
                    num +=58;
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
    public void makeFile() throws IOException {
        createDatFile();
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

    @Test
    public void test() throws IOException {
        FileReader reader = new FileReader("random_data.bin");
        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {
            int array[] = new int[256];

            for (int jj = 0; jj < 256; jj++) {
                array[jj] = reader.readNextInt();
            }
            bank.addPoint(new DataLine(array));
        }
        reader.close();
        
        for (int ii = 0; ii < 100; ii++) {
            DataLine line = bank.getNextPoint();
            System.out.println(line.getDataAt(0));

        }

    }

}
