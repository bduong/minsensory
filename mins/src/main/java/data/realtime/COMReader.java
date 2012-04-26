package data.realtime;

import data.DataReader;
import gnu.io.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class COMReader {

    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    DataInputStream dataIn = null;
    private byte [] bytes;
    private byte [] allBytes;
    private SerialPort serialPort;
    int count =0;

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
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                //serialPort.notifyOnOutputEmpty(true);
                //serialPort.notifyOnOverrunError(true);
                //serialPort.notifyOnOutputEmpty(true);
                //serialPort.notifyOnDataAvailable(true);
                serialPort.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent serialPortEvent) {
                        if(serialPortEvent.getEventType() == SerialPortEvent.OE) {
                            System.out.println("OVERRUN - " + ++count);
                        }
                    }
                });

                //in = new BufferedInputStream(serialPort.getInputStream(), 1024);
                dataIn = new DataInputStream(serialPort.getInputStream());
                out = new BufferedOutputStream(serialPort.getOutputStream(), 1024);
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
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baud,dataBits,stopBits,parity);
                //serialPort.notifyOnOutputEmpty(true);
                serialPort.notifyOnOverrunError(true);
                //serialPort.notifyOnOutputEmpty(true);
                //serialPort.notifyOnDataAvailable(true);
                serialPort.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent serialPortEvent) {
                        switch(serialPortEvent.getEventType()) {
                        case SerialPortEvent.OE:
                            System.out.println("OVERRUN - " + ++count);
                            break;
                        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                            System.out.println("Empty - " + ++count);
                            break;
                        }
                    }
                });


                serialPort.setInputBufferSize(1024);
                if(in != null) in.close();
                if(dataIn != null) dataIn.close();
                if(out != null) out.close();
                in = new BufferedInputStream(serialPort.getInputStream());
                //dataIn = new DataInputStream(serialPort.getInputStream());
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

    public int readNextInt() throws IOException {
        int num = in.read(bytes, 0, 2);
        int value = bytes[0] << 8;
        return (0x0000FFFF & ((value) | (bytes[1] & 0x000000FF)));
    }

    public int[] readAllInts(BufferedOutputStream out) throws IOException {
        int [] numbers = new int[256];
        //in.read(bytes,0,1);
        //if (bytes[0] != 0) {
        if(in.available() >= 512) {
            int num = in.read(allBytes, 0, 512);
            //System.out.println(num);
            out.write(allBytes, 0, 512);
            for (int jj = 0; jj< allBytes.length; jj+=2){
                int value = allBytes[jj] << 8;
                numbers[jj/2] =  (0x0000FFFF & ((value) | (allBytes[jj+1] & 0x000000FF)));
            }
            return numbers;
        }
        return null;
    }

//    public int[] readAllInts(BufferedOutputStream out) throws IOException {
//        int [] numbers = new int[256]; //= {0};
//
//            dataIn.readFully(allBytes, 0,512);
//            //int num = in.read(allBytes, 0, 512);
//           // System.out.println(num);
//            out.write(allBytes, 0, 512);
//            for (int jj = 0; jj< allBytes.length; jj+=2){
//                int value = allBytes[jj] << 8;
//                numbers[jj/2] =  (0x0000FFFF & ((value) | (allBytes[jj+1] & 0x000000FF)));
//            }
//        //}
//        return numbers;
//    }

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
        serialPort.close();
    }
}
