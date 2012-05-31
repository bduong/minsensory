/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package freq;

import data.playback.FileReader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FrequencyAnalysisProcessor {

    private List<List<Integer>> nodes;


    public FrequencyAnalysisProcessor() {
        nodes = new ArrayList<List<Integer>>(256);
        for (int ii = 0; ii < 256; ii++){
            nodes.add(new ArrayList<Integer>());
        }


    }

    public void printStuff() {
        for (List<Integer> node : nodes){
            System.out.println(node.size());

        }
        System.out.println(nodes.size());
        System.out.println("Done");
    }

    public void readData(File file) throws IOException {
        for (List<Integer> node : nodes) {
            node.clear();
        }

        FileReader fileReader = new FileReader(file);

        outer:
        while(true) {
            for (List<Integer> node : nodes) {
                try{
                    int dataPoint = fileReader.readNextInt();
                    node.add(dataPoint);
                } catch (IOException e) {
                    if (e.getMessage().equals("End of File")) {
                        break outer;
                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    public void runFrequencyAnalysis() {
        for(List<Integer> node : nodes) {

        }
    }

    public void saveData(File file) throws IOException {
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        int numberOfLines = nodes.get(0).size();
        byte [] bytesToWrite = new byte[512];

        for (int ii = 0; ii < numberOfLines; ii++) {
            for (int jj = 0; jj < 256; jj++) {
                int data = nodes.get(jj).get(ii);
                bytesToWrite[jj*2] = (byte) (data >> 8);
                bytesToWrite[jj*2 +1] = (byte) (data & 0x000000FF);
            }

            writer.write(bytesToWrite);

        }

    }
}
