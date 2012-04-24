package data.realtime;

import data.DataLine;
import gui.ColorMappedImage;
import gui.PlotPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class DataTimer implements ActionListener{

    private int delay;
    private Timer timer;
    private COMReader reader;
    private boolean paused;

    private PlotPanel plotPanel;
    private ColorMappedImage image;

    private byte [] bytes;
    private BufferedOutputStream outputStream;

    public DataTimer( COMReader comReader, File saveFile) throws FileNotFoundException {
        delay = 1000/30;
        paused = false;
        reader = comReader;

        bytes = new byte[2];
        outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));

        setupTimer();
    }

    public DataTimer( COMReader comReader, BufferedOutputStream saveFileStream) {
        delay = 1000/30;
        paused = false;
        reader = comReader;

        bytes = new byte[2];
        outputStream = saveFileStream;

        setupTimer();
    }

    public void setPlotPanel(PlotPanel plotPanel) {
        this.plotPanel = plotPanel;
    }

    public void setImage(ColorMappedImage colorMappedImage){
        image = colorMappedImage;
    }

    public void setSaveFileOutputStream(BufferedOutputStream outputStream) {
        this.outputStream = outputStream;
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

    public void stopTimer() {
        timer.stop();
    }

    int count = 0;
    int number =0;
    @Override
    public void actionPerformed(ActionEvent e) {
            int [] dataLine = new int[256];
//            for (int ii = 0; ii < 256; ii++) {
//                try {
//                    dataLine[ii] = reader.readNextInt();
//                    createWritableBytes(dataLine[ii]);
//                    outputStream.write(bytes);
//                } catch (IOException e1) {
//                    System.out.println("IO Exception");
//                    timer.stop();
//                }
//            }
        try{
            dataLine = reader.readAllInts(outputStream);
        } catch (IOException e1){}

        if(!paused) {
            System.out.println(count++);
            DataLine data = new DataLine(dataLine);
            plotPanel.updatePlots(data);
            image.updateImage(data);
        } else {
            plotPanel.advanceTime();
        }
    }

    private String numToString(int num){

        int value = num & 0x000000FF;
        int upper = num & 0x0000FF00;
        upper = upper >> 8;

        return "" + (char) upper + "-" + (char) value;
    }

    private void createWritableBytes(int i) {
        int upper =  i & 0x0000FF00;
        int lower = i & 0x000000FF;
        bytes[0] = (byte) (upper >> 8);
        bytes[1] = (byte) lower;
    }
}
