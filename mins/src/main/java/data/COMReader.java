package data;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class COMReader{

    InputStream in = null;
    public COMReader() {

    }

    public static List<String> listPorts() throws NoSuchPortException {
        List<String> ports = new ArrayList<String>();
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() )
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            ports.add(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );
        }
        return ports;
    }

    static String getPortTypeName ( int portType )
    {
        switch ( portType )
        {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
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

                in = serialPort.getInputStream();
               // OutputStream out = serialPort.getOutputStream();
               //(new Thread(reader)).start();
                //(new Thread(new SerialWriter(out))).start();

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

    /** */
    public static class SerialReader implements Runnable
    {
        InputStream in;

        public SerialReader ( InputStream in )
        {
            this.in = in;
        }

        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    System.out.print(new String(buffer,0,len));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

}
