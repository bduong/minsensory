package data.realtime;

import data.DataBank;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class DataTimer implements ActionListener{
    
    private int delay;
    private Timer timer;
    private COMReader reader;
    private DataBank dataBank;
    private boolean paused;

    private byte [] bytes;
    private BufferedOutputStream outputStream;

    public DataTimer(DataBank dynamicDataBank, COMReader comReader, File saveFile) throws FileNotFoundException {
        delay = 1000/30;
        paused = false;
        dataBank = dynamicDataBank;
        reader = comReader;

        bytes = new byte[2];
        outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));

        setupTimer();
    }
    
    private void setupTimer() {
        timer = new Timer(delay, this);
    }
    
    public void startTimer() throws IOException, InterruptedException {
        reader.startStream();
        timer.start();
    }
    
    public void pauseUI(){
        paused = true;
    }

    public void resumeUI() {
        paused = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int [] dataLine = new int[256];
        for (int ii = 0; ii < 256; ii++) {
            try {
                dataLine[ii] = reader.readNextInt();
                createWritableBytes(dataLine[ii]);
                outputStream.write(bytes);
            } catch (IOException e1) {
                ii--;
                continue;
            }
        }
    }

    private void createWritableBytes(int i) {
        int upper =  i & 0x0000FF00;
        int lower = i & 0x000000FF;
        bytes[0] = (byte) (upper >> 8);
        bytes[1] = (byte) lower;
    }
}
