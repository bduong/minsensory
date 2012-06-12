package com.minsensory.data.capture;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDCapture {
    BufferedInputStream in = null;
    BufferedOutputStream out = null;

    private File saveFile;
    private String portName;

    private int baud;
    private int dataBits;
    private int stopBits;
    private int parity;
    private SerialPort serialPort;
    private byte [] writeBuffer;

    private long bytesTransferred;

    private JLabel bytesLabel;

    public SDCapture() {
    }

    public void start() throws Exception {
        bytesTransferred = 0;
        writeBuffer = new byte[1024];
        connectTo(portName);
        serialPort.notifyOnDataAvailable(true);
        serialPort.setInputBufferSize(2048);
        in = new BufferedInputStream(serialPort.getInputStream());
        out = new BufferedOutputStream(new FileOutputStream(saveFile));

        serialPort.addEventListener(new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    try {
                        int bytesAvailable = in.available();
                        if (bytesAvailable > 1024) bytesAvailable = 1024;
                        if (bytesAvailable > 0) {
                            int bytesRead = in.read(writeBuffer, 0,bytesAvailable);
                            if (bytesRead > 0) {
                                out.write(writeBuffer, 0, bytesRead);
                                updateBytesRead(bytesRead);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void stop() throws IOException {
        in.close();
        serialPort.close();
        out.close();
    }

    private void updateBytesRead(int bytesRead) {
        bytesTransferred += bytesRead;
        int displayBytes;
        String suffix;

        if (bytesRead < 1024) {
            displayBytes = (int) (bytesTransferred >> 10);
            suffix = "kB";
        } else if (bytesRead < 1048576) { //1024*1024
            displayBytes = (int) (bytesTransferred >> 20);
            suffix = "MB";
        } else if (bytesRead < 1073741824) {//1024^3
            displayBytes = (int) (bytesTransferred >> 30);
            suffix = "GB";
        } else {
            displayBytes = (int) bytesTransferred;
            suffix = "B";
        }

        bytesLabel.setText(displayBytes + " " + suffix);
    }

    private void connectTo(String portName) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(SDCapture.class.getName(),2000);

            if ( commPort instanceof SerialPort)
            {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baud,dataBits,stopBits,parity);
            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }

    }

    public boolean isFinished() {
        return true;
    }

    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public void setBaud(int baud) {
        this.baud = baud;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public void setBytesLabel(JLabel bytesLabel) {
        this.bytesLabel = bytesLabel;
    }
}
