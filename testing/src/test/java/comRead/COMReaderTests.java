/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package comRead;

import data.realtime.COMReader;
import data.realtime.DataTimer;
import gnu.io.NoSuchPortException;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Test for the COMReader
 */
public class COMReaderTests {

    private Logger logger = Logger.getLogger(this.getClass());
    private COMReader comReader;
    private final String COM_NAME = "COM6";

    @BeforeClass
    void setup() {
        comReader = new COMReader();
    }

    // @TODO Mac is 32-bit fix rxtx to be compatable
    @Test
    void testPortList() throws NoSuchPortException {
        List<String> ports = COMReader.listPorts();
        
        for (String port : ports) {
            logger.info(port);
        }
    }

    @Test
    void testCOM3() throws Exception {
        comReader.connectTo(COM_NAME);
        InputStream inputStream = comReader.getInputStream();
		OutputStream outputStream = comReader.getOutputStream();
		
		int multiplier = 1000;
		int iter = 5;

        byte[] buffer = new byte[multiplier*iter];
		outputStream.write((byte) iter);
        int numOfBytes = inputStream.read(buffer, 0, multiplier*iter);
        assertEquals(numOfBytes, multiplier*iter);

		StringBuilder comp = new StringBuilder();
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		for (int ii = 0; ii < multiplier*iter; ii++) {
			comp.append(alphabet[ii % alphabet.length]);
		}

		assertEquals(buffer, comp.toString());
    }
    int count = 0;

    @Test
    void testCOMReading() throws Exception {
        comReader.connectTo(COM_NAME);
        BufferedOutputStream out = comReader.getOutputStream();
        BufferedInputStream in = comReader.getInputStream();

        //out.write("5".getBytes());
        out.close();
        logger.info("Start");
        Thread.sleep(500);

//        int [] buffer = new int[256000];
        byte [] bytes = new byte[512];
//        for (int ii = 0; ii < 256000; ii++) {
//            buffer[ii] = comReader.readNextInt();
//        }
//        for (int num : buffer){
//            logger.info(count++ + " :: " + numToString(num));
//        }
//        logger.info("Switches:" + switchcount);
//        for (int number : places){
//            logger.info(number);
//        }

        for (int ii = 0; ii <1000; ii++) {
            in.read(bytes);
            if(ii %100 == 0) {
                logger.info(ii);
            }
//            int thiscount =0;
//            for (int jj = 0; jj < bytes.length; jj+=2) {
//                if(bytes[jj] != 5)
//                thiscount++;
//            }
           // count+=thiscount;
            //logger.info(ii + " : " + thiscount);

        }
       // logger.info("Total: " + count);
        
    }


    
    int switchcount=0;
    boolean isback =false;
    List<Integer> places = new LinkedList<Integer>();
    int lastcount =0;
    private String numToString(int num){

        int value = num & 0x000000FF;
        int upper = num & 0x0000FF00;
        upper = upper >> 8;
        if(upper != 5 & !isback) {
            places.add(count-lastcount);
            lastcount=count;
            switchcount++;
            isback=true;
        }else if (upper==5 && value !=5) {
            isback=false;
        }

        return "" + upper + "-" +  value;
    }

    @Test
    void testTimedCOM() throws Exception {
        comReader.connectTo(COM_NAME);
        File file = new File("test.bin");

        DataTimer dataTimer = new DataTimer(comReader, file);
        dataTimer.startTimer();

        Thread.sleep(20000);

    }



}
