package data.realtime;

import gnu.io.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * The <code>COMReader</code> object is used to read data from the serial
 * communication port.
 *
 * It connects to a serial port with a set of parameters and then reads either
 * one 2-byte short at a time, or an array of 256 shorts at a time.
 */
public class COMReader {

    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    private byte [] bytes;
    private byte [] allBytes;
    private byte [] startByte;     //used to read start byte
    private SerialPort serialPort;
    int count =0;

    public COMReader() {
        bytes = new byte[2];
        allBytes = new byte[512];
        startByte = new byte[1];
    }

    /**
     * List the Serial Ports connected
     *
     * @return A list of the serial ports.
     * @throws NoSuchPortException If there are no ports.
     */
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

    /**
     * Connect to a specific serial port by name
     *
     * @param portName the name of the port
     * @throws Exception If we are not able to connect to the port.
     */
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

                serialPort.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent serialPortEvent) {
                        if(serialPortEvent.getEventType() == SerialPortEvent.OE) {
                            System.out.println("OVERRUN - " + ++count);
                        }
                    }
                });

                in = new BufferedInputStream(serialPort.getInputStream(), 1024);
                out = new BufferedOutputStream(serialPort.getOutputStream(), 1024);



            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }

    }

    /**
     * Connect to a specific serial port by name with given parameters.
     *
     * @param portName  the name of the port
     * @param baud the baud rate
     * @param dataBits the number of data bits
     * @param stopBits the number of stop bits
     * @param parity the number of parity bits
     * @throws Exception If we are not able to connect to the port.
     */
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
                serialPort.notifyOnOverrunError(true);
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
                if(out != null) out.close();
                in = new BufferedInputStream(serialPort.getInputStream());
                out = new BufferedOutputStream(serialPort.getOutputStream());

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }

    }

    /**
     * Get the input stream to the serial port.
     *
     * @return The input stream.
     */
    public BufferedInputStream getInputStream() {
        return in;
    }

    /**
     * Get the output stream to the serial port.
     *
     * @return The output stream.
     */
    public BufferedOutputStream getOutputStream() {
        return out;
    }

    /**
     * Read the next short integer from the stream.
     *
     * @return the next short int
     * @throws IOException If the stream cannot be read
     */
    public int readNextInt() throws IOException {
        int num = in.read(bytes, 0, 2);
        int value = bytes[0] << 8;
        return (0x0000FFFF & ((value) | (bytes[1] & 0x000000FF)));
    }

    /**
     * Read the next 256 short integers from the stream
     *
     * @param out the file output stream to write the integers to
     * @return an array of the 256 short integers
     * @throws IOException If the stream cannot be read.
     */
    public int[] readAllInts(BufferedOutputStream out) throws IOException {
        int [] numbers = new int[256];

        in.read(startByte, 0, 1);
        while(startByte[0] != 0x10) {in.read(startByte, 0, 1); };
        if (in.available() >= 512){
            int num = in.read(allBytes, 0, 512);

            out.write(allBytes, 0, 512);
            for (int jj = 0; jj< allBytes.length; jj+=2){
                int value = allBytes[jj] << 8;
                numbers[jj/2] =  (0x0000FFFF & ((value) | (allBytes[jj+1] & 0x000000FF)));
            }
            return numbers;
        }
        return null;
    }

    /**
     * Start the input stream
     *
     * @throws IOException If we cannot write to the stream.
     * @throws InterruptedException If we get interrupted while we sleep.
     */
    public void startStream() throws IOException, InterruptedException {
        out.write("5".getBytes());
        out.close();
        out = null;
        Thread.sleep(100);
    }

    /**
     * Close the serial port.
     */
    public void closeStreams(){
        try {
            out.close();
        } catch (Exception e) {}
        try{
            in.close();
        } catch (Exception e) {
        }
        serialPort.close();
    }
}
