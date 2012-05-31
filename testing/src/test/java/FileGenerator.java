/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 import data.DataBank;
import data.DataLine;
import data.playback.FileReader;
import data.playback.StaticDataBank;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static org.testng.Assert.assertEquals;

/**
 *  Tests for file generator.
 */
public class FileGenerator {

    private static final String fileName = "pretty_data.bin";
    private static final int NUMBER_OF_POINTS = 10000;

    private void createDataFile() throws IOException {
        FileOutputStream output2 = new FileOutputStream(fileName);

        Random gen = new Random();

        int array[] = new int[256];
        for (int jj = 0; jj < 256; jj++) {
            array[jj] = 1000 * (jj % 20);
        }
        boolean[] up = new boolean[256];
        for (int jj = 0; jj < 256; jj++) {
            up[jj] = true;
        }
        byte bytes[] = new byte[2];
        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {
            for (int jj = 0; jj < 256; jj++) {
                int num = array[jj] & 0x0000FFFF;
                bytes[0] = (byte) ((num & 0x0000FF00) >> 8);
                bytes[1] = (byte) (num & 0x000000FF);
                output2.write(bytes, 0, 2);
                if (up[jj]) {
                    array[jj] += 15563;
                } else {
                    array[jj] -= 26325;
                }
                if (array[jj] > 65535) {
                    up[jj] = false;
                    array[jj] -= 17885;
                } else if (array[jj] < 0) {
                    up[jj] = true;
                    array[jj] += 30254;
                }
            }

            //System.out.println(array[0] & 0x000003FF);
            if (ii % 1000 == 0)
                System.out.println(ii);

        }
        output2.close();
    }

    private void createFile() throws IOException {
        //ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName));
        FileOutputStream output2 = new FileOutputStream(fileName);

        Random gen = new Random();
        int num = gen.nextInt(65535 * 2) - 65535;

        boolean up = true;
        int array[] = new int[256];
        byte bytes[] = new byte[2];
        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {
            for (int jj = 0; jj < 256; jj++) {
                array[jj] = num & 0x0000FFFF;
                bytes[0] = (byte) ((num & 0x0000FF00) >> 8);
                bytes[1] = (byte) (num & 0x000000FF);
                output2.write(bytes);
                if (up) {
                    num += 33;
                } else {
                    num -= 33;
                }
                if (num > 65535) {
                    up = false;
                    num -= 58;
                } else if (num < 0) {
                    up = true;
                    num += 58;
                }
            }
            referenceBank.addPoint(new DataLine(array));

        }
        output2.close();
    }

    private DataBank bank;

    private DataBank referenceBank;

    private void createCoolFile() throws IOException {
        int[][] bands = {
          { 2, 2, 2, 2, 10, 10, 8, 9, 9, 1, 5, 16, 16, 16, 20, 4 }, //1
          { 2, 2, 2, 2, 10, 10, 8, 9, 9, 1, 5, 16, 16, 16, 20, 4 }, //2
          { 2, 2, 2, 10, 10, 10, 8, 9, 1, 1, 5, 16, 16, 20, 20, 4 }, //3
          { 2, 2, 2, 10, 8, 8, 8, 9, 1, 5, 5, 16, 16, 20, 20, 4 }, //4
          { 2, 2, 10, 10, 8, 9, 9, 9, 1, 5, 16, 16, 16, 20, 4, 4 }, //5
          { 2, 10, 10, 8, 8, 9, 9, 9, 1, 5, 16, 16, 20, 20, 4, 4 },  //6
          { 10, 10, 8, 8, 8, 9, 9, 1, 1, 5, 16, 16, 20, 20, 4, 4, }, //7
          { 10, 10, 8, 8, 8, 9, 9, 1, 5, 5, 16, 20, 20, 4, 4, 4 }, //8
          { 10, 8, 8, 8, 9, 9, 1, 1, 5, 16, 16, 20, 20, 4, 4, 4 }, //9
          { 10, 8, 8, 9, 9, 1, 1, 1, 5, 16, 20, 20, 4, 4, 4, 4 }, //10
          { 8, 8, 9, 9, 1, 1, 1, 1, 5, 16, 20, 20, 4, 4, 4, 4 }, //11
          { 8, 8, 9, 1, 1, 1, 1, 5, 5, 16, 20, 4, 4, 4, 4, 4 }, //12
          { 8, 9, 9, 1, 1, 1, 1, 5, 16, 16, 20, 4, 4, 4, 4, 4 }, //13
          { 8, 9, 9, 1, 1, 1, 1, 5, 16, 20, 20, 4, 4, 4, 4, 4 }, //14
          { 8, 9, 9, 1, 1, 1, 1, 5, 16, 20, 20, 4, 4, 4, 4, 4 }, //15
          { 8, 9, 9, 1, 1, 1, 1, 5, 16, 20, 20, 4, 4, 4, 4, 4 } //16
        };

        for (int ii = 0; ii < 16; ii++){
            for (int jj = 0; jj < 16; jj++) {
                bands[ii][jj] = bands[ii][jj]<<10;
            }
        }

        int [][] array = new int[16][16];
        for (int ii = 0; ii < 16; ii++){
            for (int jj = 0; jj < 16; jj++) {
                array[ii][jj] = ((ii+jj % 9)<<5) & 0x0000FFFF;
            }
        }
        boolean [][] up = new boolean[16][16];
        for (int ii = 0; ii < 16; ii++){

            for (int jj = 0; jj < 16; jj++) {
                up[ii][jj] = true;
            }
        }


        FileOutputStream output2 = new FileOutputStream(fileName);

        byte bytes[] = new byte[2];
        Random random = new Random(4L);
        System.out.println(Color.white.getRGB());
        System.out.println((new Color(255, 255, 255)).getRGB());
        for (int kk = 0; kk < NUMBER_OF_POINTS; kk++) {
            for (int ii = 0; ii < 16; ii++){
                for (int jj = 0; jj < 16; jj++) {
                    int num;
                    if (random.nextInt(Integer.MAX_VALUE) < 10000) {
                        num = Integer.MAX_VALUE;
                        System.out.println("Spike at " + kk + " : "+ ii + " " +jj);
                    } else {
                        num = (bands[ii][jj] + array[ii][jj]) & 0x0000FFFF;
                    }
                    bytes[0] = (byte) ((num & 0x0000FF00) >> 8);
                    bytes[1] = (byte) (num & 0x000000FF);
                    output2.write(bytes, 0, 2);
                    if (up[ii][jj]) {
                        array[ii][jj] += 32;
                    } else {
                        array[ii][jj] -= 32;
                    }
                    if (array[ii][jj] >= 1010) {
                        up[ii][jj] = false;
                        array[ii][jj] -=40;
                    } else if (array[ii][jj] < 128) {
                        up[ii][jj] = true;
                        array[ii][jj] += 40;
                    }
//                    int num = ((bands[ii][jj]) & 0x0000FFFF) + 512;
//                    bytes[0] = (byte) ((num & 0x0000FF00) >> 8);
//                    bytes[1] = (byte) (num & 0x000000FF);
//                    output2.write(bytes, 0, 2);

                }

            }
            if (kk % 1000 == 0)
                System.out.println(kk);

        }

        output2.close();


    }

    @BeforeMethod
    void setup() {
        bank = new StaticDataBank();
        referenceBank = new StaticDataBank();
    }

    @Test
    public void makeFile() throws IOException {
        createDataFile();
    }

    @Test
    public void makeCoolFile() throws IOException {
        createCoolFile();
    }

    @Test
    public void testRead() throws IOException {
        createFile();
        FileReader reader = new FileReader(fileName);
        int array[] = new int[256];
        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {

            for (int jj = 0; jj < 256; jj++) {
                array[jj] = reader.readNextInt();
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

    @Test(expectedExceptions = IOException.class)
    public void testReadAllPoints() throws IOException {
        createFile();
        //logger.info("created file");
        FileReader reader = new FileReader(fileName);
        int array[] = new int[256];
        int count = 0;
        while (count++ < NUMBER_OF_POINTS + 10000) {

            for (int jj = 0; jj < 256; jj++) {
                array[jj] = reader.readNextInt();
            }
           // if (count % 1000 == 0) logger.info(String.valueOf(count));
        }

        System.out.println(count);
    }

}
