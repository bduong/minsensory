package com.minsensory;

import com.minsensory.freq.FrequencyAnalysisProcessor;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.testng.annotations.Test;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class FrequencyAnalysisTests {

    private static final int NUMBER_OF_POINTS = 200000;
    private static final String fileName = "test-data.bin";
    private static final double POINTS_PER_SECOND = 20000;
    private static final int FREQUENCY = 10;
    Logger logger = Logger.getLogger(FrequencyAnalysisTests.class);

    @Test
    public void testReadFileCorrectly() throws IOException {
        File file = new File(fileName);
        BufferedOutputStream out = new BufferedOutputStream(
          new FileOutputStream(file));
        byte[] bytes = new byte[2];

        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {
            for (int jj = 1; jj <= 256; jj++) {
                bytes[0] = (byte) (jj >> 8);
                bytes[1] = (byte) (jj & 0x000000FF);
                out.write(bytes);
            }
        }
        out.close();

        FrequencyAnalysisProcessor processor = new FrequencyAnalysisProcessor();
        processor.setLoadFile(file);
        processor.readData(NUMBER_OF_POINTS);
        List<List<Integer>> nodes = processor.getNodes();

        for (int kk = 0; kk < 256; kk++) {
            logger
              .info("Checking node: " + kk + " Size = " + nodes.get(kk).size());
            for (int data : nodes.get(kk)) {
                assertEquals(data, kk + 1, "Node " + kk + " does not match");
            }
        }

    }

    @Test
    public void canRunFrequencyAnalysis() throws IOException {
        File file = new File(fileName);
        BufferedOutputStream out = new BufferedOutputStream(
          new FileOutputStream(file));
        byte[] bytes = new byte[2];

        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {
            for (int jj = 1; jj <= 256; jj++) {
                bytes[0] = (byte) (jj >> 8);
                bytes[1] = (byte) (jj & 0x000000FF);
                out.write(bytes);
            }
        }
        out.close();

        FrequencyAnalysisProcessor processor = new FrequencyAnalysisProcessor();
        processor.init();
        processor.setLoadFile(file);
        processor.readData(NUMBER_OF_POINTS);
        processor.runFrequencyAnalysis();
    }

    public static void main(String[] args) throws IOException {
        File file = new File(fileName);
        BufferedOutputStream out = new BufferedOutputStream(
          new FileOutputStream(file));
        byte[] bytes = new byte[2];
        //float [] buffer = new float[40000];

        XYSeries original = new XYSeries("Original");

        for (int ii = 0; ii < NUMBER_OF_POINTS; ii++) {
            int y = getYFromSin(ii, FREQUENCY);
            original.add(ii, y &0x00000FFF);
            for (int jj = 1; jj <= 256; jj++) {

                    bytes[0] = (byte) ((y >> 8) & 0x0000000F);
                    bytes[1] = (byte) (y & 0x000000FF);

                out.write(bytes);
            }
        }
        out.close();

        FrequencyAnalysisProcessor processor = new FrequencyAnalysisProcessor();
        processor.init();
        processor.setLoadFile(file);
        processor.readData(NUMBER_OF_POINTS);
        float[] buffer = processor.runFrequencyAnalysisTest();
        float max = 0;
        int maxIndex = 0;
        for (int ii = 1; ii < buffer.length; ii++) {
            if (Math.abs(buffer[ii]) > max) {
                max = Math.abs(buffer[ii]);
                maxIndex=ii;
            }
        }

        System.out.println("Max is at: " + maxIndex/2 + " Hz");


        List<Integer> node = processor.getNodes().get(255);
        XYSeries xySeries = new XYSeries("FFT");
        XYSeries sin = new XYSeries("Sin Wave");

        for (int ii = 0; ii < buffer.length; ii++) {
            xySeries.add(ii, buffer[ii]);
        }
        for (int ii = 0; ii < node.size(); ii++){
            sin.add(ii, node.get(ii));
        }

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection(
          xySeries);
        ChartPanel freqChartPanel = new ChartPanel(
          ChartFactory.createXYLineChart(
            null,
            null,
            null,
            xySeriesCollection,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
          ));
        XYSeriesCollection xySeriesCollection2 = new XYSeriesCollection(
          sin);
        ChartPanel freqChartPanel2 = new ChartPanel(
          ChartFactory.createXYLineChart(
            null,
            null,
            null,
            xySeriesCollection2,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
          ));

        XYSeriesCollection xySeriesCollection3 = new XYSeriesCollection(
          original);
        ChartPanel freqChartPanel3 = new ChartPanel(
          ChartFactory.createXYLineChart(
            null,
            null,
            null,
            xySeriesCollection3,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
          ));

        freqChartPanel.getChart().getXYPlot().getDomainAxis()
          .setRange(-21000, 21000);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(freqChartPanel);
        panel.add(freqChartPanel2);
        panel.add(freqChartPanel3);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static int getYFromSin(double x, int frequency) {
         double length = POINTS_PER_SECOND/(double)frequency;

         return (int) (100*Math.sin(x * (2*Math.PI/length))) +100;
    }

    public static void main2 (String [] args){
        double length = 5;

        float [] points = new float[40000];

        for (int ii = 0; ii > 40000; ii++){

        }



    }

}
