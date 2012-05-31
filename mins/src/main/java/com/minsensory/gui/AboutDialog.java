/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package com.minsensory.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The <code>AboutDialog</code> object displays information of the developers and creators of this project.
 */
public class AboutDialog extends JFrame {

    public AboutDialog(){
        super();
        setTitle("About");
        createDialog();
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    /**
     * Create the AboutDialog the display the application information.
     */
    private void createDialog() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        ImageIcon icon = new ImageIcon(AboutDialog.class.getResource("/TaskbarIcon.png"));
        JLabel imageIcon = new JLabel(icon);
        imageIcon.setPreferredSize(new Dimension(256, 256));
        add(imageIcon);
        setIconImage(icon.getImage());

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("MINSensory", JLabel.CENTER);
        JLabel version = new JLabel("Version 1.0", JLabel.CENTER);
        String about = "Created by \n\n Ben Duong \n Nima Haghighi-Mood \n Michael Kasparian \n\n ECE @ BU 2012 \n\n";
        String summary =
          "Team MINSensory aims to provide a complete interface suite that will allow for " +
          "real-time collection, analysis, and visualization of neural signals. " +
          "Using the newly developed Multiple-Input Neural Sensor Integrated Circuit (MINS IC), an array of 16x16 neural" +
          "signals can be captured at high fidelity and processed through a hardware and software interface. This" +
          "interface will consist of a daughter board for real time collection and analysis as well as a software suite to" +
          "visualize and store incoming data for users. The hardware interface consists of a custom designed PCB allowing " +
          "for capture and processing of analog signals using digital signal processing techniques that yield fast analysis " +
          "on the data. The software interface will include both a color-mapped grid, displaying the relative intensity of " +
          "all 256 nodes, as well as five user- selectable waveform plots of voltage vs. time. Along with providing real-time " +
          "visualizations, this system will also provide real-time activity recognition in the form of neural band identification. " +
          "This combination of high fidelity neural sensing, real-time activity recognition, and real-time visualizations will " +
          "provide researchers with unprecedented control and depth in their neural experimentation and " +
          "will expand avenues for neural research.";

        JTextArea aboutText = new JTextArea(about + summary);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setEditable(false);
        Font font = new Font("Arial", Font.BOLD, 12);
        aboutText.setFont(font);
        aboutText.setForeground(Color.GRAY);
        JScrollPane aboutPane = new JScrollPane(aboutText);

        textPanel.add(title);
        textPanel.add(version);
        textPanel.add(aboutPane);
        textPanel.setPreferredSize(new Dimension(256, 256));
        add(textPanel);
    }
}
