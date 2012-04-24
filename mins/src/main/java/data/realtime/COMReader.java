package data.realtime;

import data.DataReader;
import gnu.io.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class COMReader implements DataReader {

    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    private byte [] bytes;
    private byte [] allBytes;
    public COMReader() {
        bytes = new byte[2];
        allBytes = new byte[512];
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
    int count =0;
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
                serialPort.notifyOnOverrunError(true);
                serialPort.notifyOnOutputEmpty(true);
                serialPort.notifyOnDataAvailable(true);
                serialPort.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent serialPortEvent) {
                        if(serialPortEvent.getEventType() == SerialPortEvent.OE) {
                            System.out.println("OVERRUN - " + ++count);
                        }
                    }
                });

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

    public void connectTo(String portName, int baud, int dataBits , int stopBits, int parity) throws Exception
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
                serialPort.setSerialPortParams(baud,dataBits,stopBits,parity);
                serialPort.notifyOnOutputEmpty(true);
                serialPort.notifyOnOverrunError(true);
                serialPort.notifyOnOutputEmpty(true);
                serialPort.notifyOnDataAvailable(true);
                serialPort.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent serialPortEvent) {
                        if(serialPortEvent.getEventType() == SerialPortEvent.OE) {
                            System.out.println("OVERRUN - " + ++count);
                        }
                    }
                });

                if(in != null) in.close();
                if(out != null) out.close();
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

    public BufferedInputStream getInputStream() {
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

    public int[] readAllInts(BufferedOutputStream out) throws IOException {
        int [] numbers = new int[256];
        int num = in.read(allBytes, 0, 512);
        out.write(allBytes, 0, 512);
        for (int jj = 0; jj< allBytes.length; jj+=2){
            int value = allBytes[jj] << 8;
            numbers[jj/2] =  (0x0000FFFF & ((value) | (allBytes[jj+1] & 0x000000FF)));
        }
        return numbers;
    }

    public void startStream() throws IOException, InterruptedException {
        out.write("5".getBytes());
        out.close();
        out = null;
        Thread.sleep(100);
    }

    public void closeStreams(){
        try {
            out.close();
        } catch (Exception e) {
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try{
            in.close();
        } catch (Exception e) {
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
