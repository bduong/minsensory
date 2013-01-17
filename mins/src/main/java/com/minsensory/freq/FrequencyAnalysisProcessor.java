/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

package com.minsensory.freq;

import com.minsensory.data.playback.FileReader;
import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FrequencyAnalysisProcessor {


    BufferedOutputStream writer;
    private static final int NUMBER_OF_POINTS_PER_ITERATION = 20000;
    private static final int NUMBER_OF_NODES = 256;

    private List<List<Integer>> nodes;
    private FileReader fileReader;
    private FloatFFT_1D fft;
    private byte [] flags;
    private float[] buffer;

    private float alphaThreshold;
    private float betaThreshold;
    private float deltaThreshold;
    private float thetaThreshold;
    private float gammaThreshold;

    private long fileSize;

    public FrequencyAnalysisProcessor() {

    }

    public void init(){
        nodes = new ArrayList<List<Integer>>(NUMBER_OF_NODES);
        for (int ii = 0; ii < NUMBER_OF_NODES; ii++){
            nodes.add(new ArrayList<Integer>());
        }

        buffer = new float[NUMBER_OF_POINTS_PER_ITERATION * 2];

        fft = new FloatFFT_1D(NUMBER_OF_POINTS_PER_ITERATION);
        flags = new byte[NUMBER_OF_NODES];
    }

    public void run() throws IOException {
//        boolean notEOF = true;
//        while (notEOF) {
//            notEOF = readData(NUMBER_OF_POINTS_PER_ITERATION);
//            runFrequencyAnalysis();
//            saveData();
//        }
//        writer.close();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Frequency Analysis");
                ProgressPanel progressPanel = new ProgressPanel();
                progressPanel.setOpaque(true);
                frame.setContentPane(progressPanel);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }

        });

    }

    private void clearFlags() {
        for (int ii = 0; ii < NUMBER_OF_NODES; ii++) {
            flags[ii] = (byte) 0;
        }
    }

    public void printStuff() {
        for (List<Integer> node : nodes){
            System.out.println(node.size());

        }
        System.out.println(nodes.size());
        System.out.println("Done");
    }

    public boolean readData(int numberOfPoints) throws IOException {
        for (List<Integer> node : nodes) {
            node.clear();
        }

        for (int ii = 0; ii < numberOfPoints; ii++){
            for (List<Integer> node : nodes) {
                try{
                    int dataPoint = fileReader.readNextInt();
                    node.add(dataPoint);
                } catch (IOException e) {
                    if (e.getMessage().equals("End of File")) {
                        return false;
                    } else {
                        throw e;
                    }
                }
            }
        }
        return true;
    }

    public void setLoadFile(File file) throws IOException {
        if(fileReader != null) fileReader.close();
        fileSize = file.length();
        fileReader = new FileReader(file);

    }

    public float[] runFrequencyAnalysisTest() {
        clearFlags();
        for(List<Integer> node : nodes) {
            for (int ii = 0; ii< 20000; ii++){
                buffer[ii] = (float) node.get(ii);
            }


            fft.realForwardFull(buffer);
            float max = 0;

            for(int ii = 0; ii < buffer.length/2; ii+=2) {
                if(buffer[ii] > max) max = buffer[ii];
            }


        }
        return buffer;
    }

    public void runFrequencyAnalysis() {
        clearFlags();
        for(int nodeNumber = 0; nodeNumber < NUMBER_OF_NODES; nodeNumber++){
            List<Integer> node = nodes.get(nodeNumber);
            for (int ii = 0; ii< NUMBER_OF_POINTS_PER_ITERATION; ii++){
                buffer[ii] = (float) node.get(ii);
            }


            fft.realForwardFull(buffer);
            float max = 0;
            for(int ii = 0; ii < buffer.length/2; ii+=2) {
                if(buffer[ii] > max) max = buffer[ii];
            }

            setFlags(nodeNumber, max);
        }
    }

    private void setFlags(int nodeNumber, float max) {
        //alpha band    8-12Hz
        for (int freq = 9; freq <= 12; freq++){
            if (isEnergyAboveThreshold(max, freq, alphaThreshold)) {
                flags[nodeNumber] |= 0x04;
            }
        }

        //beta band
        for (int freq = 13; freq <= 30; freq++) {
            if (isEnergyAboveThreshold(max, freq, betaThreshold)) {
                flags[nodeNumber] |= 0x08;
            }
        }

        //delta band
        for (int freq = 1; freq <= 4; freq++) {
            if (isEnergyAboveThreshold(max, freq, deltaThreshold)) {
                flags[nodeNumber] |= 0x20;
            }
        }

        //gamma band
        for (int freq = 31; freq<= 70; freq++){
            if (isEnergyAboveThreshold(max, freq, gammaThreshold)) {
                flags[nodeNumber] |= 0x10;
            }
        }

        //theta band
        for (int freq = 5; freq <= 8; freq++) {
            if (isEnergyAboveThreshold(max, freq, thetaThreshold)) {
                flags[nodeNumber] |= 0x80;
            }
        }
    }

    private boolean isEnergyAboveThreshold(float max, int freq, float threshold) {
        return Math.abs(buffer[2*freq]) + Math.abs(buffer[2*freq+1]) > max * threshold;
    }

    private void saveData() throws IOException {
        int numberOfLines = nodes.get(0).size();
        byte [] bytesToWrite = new byte[NUMBER_OF_NODES*2];

        for (int ii = 0; ii < numberOfLines; ii++) {
            for (int jj = 0; jj < NUMBER_OF_NODES; jj++) {
                byte flag = flags[jj];
                int data = nodes.get(jj).get(ii);
                bytesToWrite[jj*2] = (byte) ((byte) ((data >> 10) & 0x000003) | flag);
                bytesToWrite[jj*2 +1] = (byte) ((data>>2) & 0x000000FF);
            }

            writer.write(bytesToWrite);
        }

    }

    public void setSaveFile(File file) throws FileNotFoundException {
        writer = new BufferedOutputStream(new FileOutputStream(file));
    }

    public List<List<Integer>> getNodes() {
        return nodes;
    }

    public void setAlphaThreshold(float alphaThreshold) {
        this.alphaThreshold = alphaThreshold;
    }

    public void setBetaThreshold(float betaThreshold) {
        this.betaThreshold = betaThreshold;
    }

    public void setDeltaThreshold(float deltaThreshold) {
        this.deltaThreshold = deltaThreshold;
    }

    public void setThetaThreshold(float thetaThreshold) {
        this.thetaThreshold = thetaThreshold;
    }

    public void setGammaThreshold(float gammaThreshold) {
        this.gammaThreshold = gammaThreshold;
    }

    private class ProgressPanel extends JPanel implements ActionListener, PropertyChangeListener {

        private JButton startButton;
        private JButton cancelButton;
        private JProgressBar progressBar;
        private JTextArea taskOutput;
        private Task task;

        public ProgressPanel() {
            super();
            super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            startButton = new JButton("Start");
            startButton.setActionCommand("start");
            startButton.addActionListener(this);

            cancelButton = new JButton("Cancel");
            cancelButton.setActionCommand("cancel");
            cancelButton.addActionListener(this);

            progressBar = new JProgressBar(0, 100);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);

            taskOutput = new JTextArea(5, 20);
            taskOutput.setMargin(new Insets(5, 5, 5, 5));
            taskOutput.setEditable(false);

            JPanel panel = new JPanel();
            panel.add(startButton);
            panel.add(cancelButton);
//            panel.add(progressBar);

            add(panel);
            add(progressBar);
            add(new JScrollPane(taskOutput));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        class Task extends SwingWorker<Void, Void> {
            /*
            * Main task. Executed in background thread.
            */
            @Override
            public Void doInBackground() {
                Random random = new Random();
                int progress = 0;
                //Initialize progress property.
                setProgress(0);
                while (progress < 100) {
                    if(isCancelled()) break;
                    //Sleep for up to one second.
                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException ignore) {
                        break;
                    }
                    //Make random progress.
                    progress += random.nextInt(10);
                    setProgress(Math.min(progress, 100));
                }
                return null;
            }

            /*
            * Executed in event dispatching thread
            */
            @Override
            public void done() {
                Toolkit.getDefaultToolkit().beep();
                cancelButton.setEnabled(false);
                setCursor(null);
                taskOutput.append("Done!\n");
            }
        }



        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if("start".equals(actionEvent.getActionCommand())) {
                cancelButton.setEnabled(true);
                startButton.setEnabled(false);

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                task = new Task();
                task.addPropertyChangeListener(this);
                task.execute();

            } else if("cancel".equals(actionEvent.getActionCommand())) {
                cancelButton.setEnabled(false);
                task.cancel(true);
            }

        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("progress".equals(evt.getPropertyName())) {
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
                taskOutput.append(String.format(
                  "Completed %d%% of task.\n", task.getProgress()));
            }
        }
    }

}

