/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package com.minsensory.ui;


import com.minsensory.freq.FrequencyAnalysisProcessor;

public class UITests {


//    public static void main(String args []) {
//        JFrame frame = new JFrame("TabbedPaneDemo");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        FrequencyAnalysisPanel panel = new FrequencyAnalysisPanel();
//
//        panel.setSaveFileSelected(new JLabel("File Name"));
//        panel.setSelectSaveFileButton(new JButton("Select"));
//        JFormattedTextField alpha =  new JFormattedTextField();
//        alpha.setValue(50);
//        alpha.setColumns(3);
//        panel.setAlphaField(alpha);
//
//        JFormattedTextField beta =  new JFormattedTextField();
//        beta.setValue(50);
//        beta.setColumns(3);
//        panel.setBetaField(beta);
//
//        JFormattedTextField delta =  new JFormattedTextField();
//        delta.setValue(50);
//        delta.setColumns(3);
//        panel.setDeltaField(delta);
//
//        JFormattedTextField gamma =  new JFormattedTextField();
//        gamma.setValue(50);
//        gamma.setColumns(3);
//        panel.setGammaField(gamma);
//
//        JFormattedTextField theta =  new JFormattedTextField();
//        theta.setValue(50);
//        theta.setColumns(3);
//        panel.setThetaField(theta);
//
//
//        panel.setAlphaSlider(new JSlider(0,100,50));
//        panel.setBetaSlider(new JSlider(0,100,50));
//        panel.setDeltaSlider(new JSlider(0,100,50));
//        panel.setGammaSlider(new JSlider(0,100,50));
//        panel.setThetaSlider(new JSlider(0,100,50));
//
//        panel.setProcessButton(new JButton("Process"));
//
//        Object [][] data = {
//          {"Alpha", "8-12 Hz"},
//          {"Beta", "13-30 Hz"},
//          {"Delta", "1-4 Hz"},
//          {"Gamma", "30-70 Hz"},
//          {"Theta", "4-8 Hz"}
//        };
//
//        panel.setBandColorTable(new JTable(data,new String[]{"Band", "Freq & Color"}));
//
//        panel.setFrequencyChart(new ChartPanel(ChartFactory.createXYLineChart(
//          "Dummy",
//          "Frequency",
//          "Amp",
//          new XYSeriesCollection(new XYSeries("Stuff")),
//          PlotOrientation.VERTICAL,
//          false,
//          true,
//          false
//        )
//        )
//
//        );
//
//         panel.layoutComponents();
//
//        //Add content to the window.
////        frame.add(new SelectionPanel().getPlayOptions(), BorderLayout.CENTER);
//        frame.add(panel, BorderLayout.CENTER);
//        frame.setLocationRelativeTo(null);
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
//    }

    public static void main(String [] args) {
        FrequencyAnalysisProcessor freq = new FrequencyAnalysisProcessor();
        freq.printStuff();
    }
}
