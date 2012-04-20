package gui.menu.items;

import data.realtime.COMReader;
import gnu.io.NoSuchPortException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class ValidationTestPanel extends JPanel {

    private JLabel vrefLabel;
    private JComboBox vrefBox;

    private JLabel comLabel;
    private JComboBox comBox;

    private JButton test;
    private JButton disconnect;

    private JTextArea textArea;

    private JLabel voltageReadingLabel;
    private JLabel voltageReading;

    private COMReader comReader;
    private Timer timer;
    private double vref;

    private int displayCount =0;

    public ValidationTestPanel() {
        init();
    }

    public void init() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);


        vrefLabel = new JLabel("Reference Voltage");
        vrefBox = new JComboBox(new String[]{"2.5", "3.3", "5"});
        vrefBox.setEditable(true);
        vrefBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vref = Double.valueOf((String) vrefBox.getSelectedItem());
            }
        });

        comLabel = new JLabel("COM");
        try {
            List<String> ports = COMReader.listPorts();
            if (ports.size() <= 0) {
                comBox = new JComboBox(new String[]{""});
            } else {
                String[] portNames = new String[ports.size()];
                ports.toArray(portNames);
                comBox = new JComboBox(portNames);
            }
        } catch (NoSuchPortException e) {
            comBox = new JComboBox(new String[]{""});
        }



        timer = new Timer(1000/30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    int value = comReader.readNextInt();
                    value = value & 0x00000FFF;
                    if(++displayCount % 30 == 0) {
                        displayCount = 0;
                        double voltage = vref*value;


                        if( voltage < .99)
                            voltageReading.setText(String.format("%.2f mV", voltage*1000));
                        else
                            voltageReading.setText(String.format("%.2f V", voltage));

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });

        textArea = new JTextArea("Testing at: \n\n Baud:     \n Data Bits:      \n Stop Bits:     \n Parity:      \n");
        textArea.setPreferredSize(new Dimension(50, 50));

        test = new JButton("Test");
        test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!comBox.getSelectedItem().equals("")) {
                    comReader = new COMReader();
                    try {
                        comReader.connectTo((String) comBox.getSelectedItem());
                        comReader.startStream();
                        timer.start();
                    } catch (Exception e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    test.setEnabled(false);
                    comBox.setEnabled(false);
                    disconnect.setEnabled(true);
                    textArea.setText("Testing Port:"+ "\n" +comBox.getSelectedItem() + "\n\n" + " Baud: 9600 \n Data Bits: 8 \n Stop Bits: 1 \n Parity: None \n");
                }
            }
        }
        );

        disconnect = new JButton("Disconnect");
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect.setEnabled(false);
                test.setEnabled(true);
                timer.stop();
            }
        });

        voltageReadingLabel = new JLabel("Voltage Level:");
        voltageReading = new JLabel("");

        layout.setVerticalGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
              .addGap(5, 10, 20)
              .addComponent(vrefLabel)
              .addComponent(vrefBox)
              .addGap(5, 10, 20)
              .addComponent(comLabel)
              .addComponent(comBox)
              .addGap(5, 10, 20)
            )
            .addGroup(layout.createSequentialGroup()
              .addGap(5, 10, 20)
              .addComponent(voltageReadingLabel)
              .addComponent(voltageReading)
              .addGap(5, 10, 20)
            )
          )
          .addGap(5, 10, 20)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
              .addComponent(test)
              .addComponent(disconnect)
            )
            .addComponent(textArea)
          )
        );

        layout.setHorizontalGroup(layout.createSequentialGroup()
          .addGap(0, 0, 10)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(vrefLabel)
            .addComponent(vrefBox)
            .addComponent(comLabel)
            .addComponent(comBox)
            .addComponent(test)
            .addComponent(disconnect)
          )
          .addGap(0, 10, 20)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(voltageReadingLabel)
            .addComponent(voltageReading)
            .addComponent(textArea)
          )
          .addGap(0, 0, 10)
        );

        layout.linkSize(SwingConstants.HORIZONTAL, textArea, vrefBox, comBox, vrefLabel, voltageReadingLabel, comLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, test, disconnect);
    }

    public void shutdownClean() {
        if(timer != null)
            timer.stop();
        if(comReader != null)
            comReader.closeStreams();
    }

}
