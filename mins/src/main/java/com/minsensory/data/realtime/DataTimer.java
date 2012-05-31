package com.minsensory.data.realtime;

import com.minsensory.data.DataLine;
import com.minsensory.gui.ColorMappedImage;
import com.minsensory.gui.PlotPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * The <code>DataTimer</code> object is used to update the image and plots in the UI
 * while processing the data coming from the serial COM port in real time mode.
 *
 * Every 1/30 of a second, it calls into the given <code>COMReader</code> object
 * to get the next set of 256 short integers. It then passes this data immediately
 * to the <code>ColorMappedImage</code> and <code>PlotPanel</code> on the UI to
 * update the information.
 */
public class DataTimer implements ActionListener{

    private int delay;
    private Timer timer;
    private COMReader reader;
    private boolean paused;

    private PlotPanel plotPanel;
    private ColorMappedImage image;

    private byte [] bytes;
    private BufferedOutputStream outputStream;
    private long time = 0;

    /**
     * Creates a new <code>DataTimer</code> with a given <code>COMReader</code> and file to save to.
     *
     * @param comReader the COMReader
     * @param saveFile the file to save to
     * @throws FileNotFoundException If the save file is not found.
     */
    public DataTimer( COMReader comReader, File saveFile) throws FileNotFoundException {
        delay = 1000/30;
        paused = false;
        reader = comReader;

        bytes = new byte[2];
        outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));

        setupTimer();
    }

    /**
     * Creates a new <code>DataTimer</code> with a given <code>COMReader</code> and the save file stream to save to.
     *
     * @param comReader the COMReader
     * @param saveFileStream the file stream to save to
     */
    public DataTimer( COMReader comReader, BufferedOutputStream saveFileStream) {
        delay = 1000/30;
        paused = false;
        reader = comReader;

        bytes = new byte[2];
        outputStream = saveFileStream;

        setupTimer();
    }

    /**
     * Sets the plot panel to update.
     *
     * @param plotPanel the plot panel to update
     */
    public void setPlotPanel(PlotPanel plotPanel) {
        this.plotPanel = plotPanel;
    }

    /**
     * Sets the image to update.
     *
     * @param colorMappedImage the color mapped image to update.
     */
    public void setImage(ColorMappedImage colorMappedImage){
        image = colorMappedImage;
    }

    /**
     * Sets the save file output stream.
     *
     * @param outputStream the save file output stream.
     */
    public void setSaveFileOutputStream(BufferedOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Setup the timer.
     */
    private void setupTimer() {
        timer = new Timer(delay, this);
        time = System.currentTimeMillis();
    }

    /**
     * Start the timer and the read stream.
     *
     * @throws IOException If we cannot write to file.
     * @throws InterruptedException If we get interrupted while starting the stream.
     */
    public void startTimer() throws IOException, InterruptedException {
        reader.startStream();
        timer.start();
    }

    /**
     * Pause the updating of the IU.
     */
    public void pauseUI(){
        paused = true;
    }

    /**
     * Resume the updating of the IU.
     */
    public void resumeUI() {
        if (!timer.isRunning()) timer.start();
        paused = false;
    }

    /**
     * Stop the timer.
     */
    public void stopTimer() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            int [] dataLine = new int[256];
        try{
          dataLine = reader.readAllInts(outputStream);
        } catch (IOException e1){ }

        long thisTime = System.currentTimeMillis();
        System.out.println(thisTime-time + " ms");
        time = thisTime;

        if(!paused && dataLine != null) {
            plotPanel.updatePlots(new DataLine(dataLine));
            image.updateImage(new DataLine(dataLine));
        } else {
            plotPanel.advanceTime();
        }
    }
}
