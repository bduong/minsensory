package comRead;

import data.realtime.COMReader;
import gnu.io.NoSuchPortException;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import type.TestType;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static org.testng.Assert.assertEquals;

@Test(groups = TestType.ALL)
public class COMReaderTests {

    private Logger logger = Logger.getLogger(this.getClass());
    private COMReader comReader;
    private final String COM_NAME = "COM3";

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

    @Test
    void testCOMReading() throws Exception {
        comReader.connectTo(COM_NAME);
        BufferedOutputStream out = comReader.getOutputStream();

 //       byte [] bytes = new byte[1];
//        bytes[0] = 5;
        out.write("5".getBytes());

        out.close();
//        int buffer = comReader.readNextInt();
//        logger.info(buffer);
        Thread.sleep(500);
        int [] buffer = new int[5000];
        for (int ii = 0; ii < 5000; ii++) {
            buffer[ii] = comReader.readNextInt();
    }
        int count = 0;
        for (int num : buffer){
            logger.info(count++ + " :: " + numToString(num));
        }
    }
    
    private String numToString(int num){

        int value = num & 0x000000FF;
        int upper = num & 0x0000FF00;
        upper = upper >> 8;

        return "" + (char) upper + "-" + (char) value;
    }



}
