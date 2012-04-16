package data.realtime;

import data.DataReader;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class COMReader implements DataReader {

    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    private byte [] bytes;
    public COMReader() {
          bytes = new byte[2];
    }

    public static List<String> listPorts() throws NoSuchPortException {
        List<String> ports = new ArrayList<String>();
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() )
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                ports.add(portIdentifier.getName());
            }
        }
        return ports;
    }

    public void connectTo(String portName) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(COMReader.class.getName(),2000);

            if ( commPort instanceof SerialPort)
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                serialPort.notifyOnOutputEmpty(true);

                in = new BufferedInputStream(serialPort.getInputStream());
		        out = new BufferedOutputStream(serialPort.getOutputStream());
                //(new Thread(new SerialWriter(out))).start();

//                serialPort.addEventListener(new SerialReader(in));
//                serialPort.notifyOnDataAvailable(true);


            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }

    }

    public InputStream getInputStream() {
        return in;
    }

    public BufferedOutputStream getOutputStream() {
	return out;
    }

    @Override
    public int readNextInt() throws IOException {
        int num = in.read(bytes, 0, 2);
        int value = bytes[0] << 8;
        return (0x0000FFFF & ((value) | (bytes[1] & 0x000000FF)));
    }

    public void startStream() throws IOException, InterruptedException {
        out.write("5".getBytes());
        out.close();
        Thread.sleep(100);
    }
}
