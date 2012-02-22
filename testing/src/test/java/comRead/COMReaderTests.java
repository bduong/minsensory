package comRead;

import data.COMReader;
import gnu.io.NoSuchPortException;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import type.TestType;

import java.io.InputStream;
import java.util.List;

@Test(groups = TestType.ALL)
public class COMReaderTests {

    private Logger logger = Logger.getLogger(this.getClass());
    private COMReader comReader;
    private final String COM_NAME = "COM3";

    @BeforeClass
    void setup() {
        comReader = new COMReader();
    }

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
        byte[] buffer = new byte[256];
        inputStream.read(buffer);

        for (byte buf : buffer) {
            logger.info(buf);
        }
    }

}
