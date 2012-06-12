/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package com.minsensory.freq;

import com.minsensory.data.playback.FileReader;
import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        boolean notEOF = true;
        while (notEOF) {
            notEOF = readData(NUMBER_OF_POINTS_PER_ITERATION);
            runFrequencyAnalysis();
            saveData();
        }
        writer.close();
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
}
