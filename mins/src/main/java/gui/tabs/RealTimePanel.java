/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package gui.tabs;

import javax.swing.*;


public class RealTimePanel extends JPanel {

    private JButton connect;
    private JButton disconnect;

    private JLabel comLabel;
    private JComboBox comBox;

    private JButton startCOM;
    private JButton stopCOM;

    private JLabel readingStatus;

    private JButton save;
    private JLabel fileNameSave;

    private JLabel baudLabel;
    private JComboBox baudComboBox;

    private JLabel dataLabel;
    private JComboBox dataComboBox;

    private JLabel stopBitsLabel;
    private JComboBox stopBitsComboBox;

    private JLabel parityLabel;
    private JComboBox parityComboBox;

    public RealTimePanel() {
        comLabel = new JLabel("COM");
        baudLabel = new JLabel("BAUD");
        dataLabel = new JLabel("DataBits");
        stopBitsLabel = new JLabel("Stop Bits");
        parityLabel = new JLabel("Parity Bit");
    }

    public void layoutComponents() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHonorsVisibility(false);

        layout.setVerticalGroup(layout.createSequentialGroup()
          .addGap(0, 5, 100)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(startCOM)
            .addComponent(stopCOM)
            .addComponent(save)
          )
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(readingStatus)
            .addComponent(fileNameSave)
          )
          .addGap(0, 10, 100)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
              .addComponent(connect)
              .addComponent(disconnect)
            )
            .addGroup(layout.createSequentialGroup()
              .addComponent(comLabel)
              .addComponent(comBox)
            )
            .addGroup(layout.createSequentialGroup()
              .addComponent(baudLabel)
              .addComponent(baudComboBox)
            )
            .addGroup(layout.createSequentialGroup()
              .addComponent(dataLabel)
              .addComponent(dataComboBox)
            )
            .addGroup(layout.createSequentialGroup()
              .addComponent(stopBitsLabel)
              .addComponent(stopBitsComboBox)
            )
            .addGroup(layout.createSequentialGroup()
              .addComponent(parityLabel)
              .addComponent(parityComboBox)
            )
          )
          .addGap(0, 5, 100)
        );

        layout.linkSize(SwingConstants.VERTICAL, startCOM, stopCOM);
        layout.linkSize(SwingConstants.VERTICAL, startCOM, save);
        layout.linkSize(SwingConstants.VERTICAL, readingStatus, fileNameSave);

        layout.linkSize(SwingConstants.VERTICAL, connect, disconnect);

        layout.linkSize(SwingConstants.VERTICAL, connect, comLabel);
        layout.linkSize(SwingConstants.VERTICAL, connect, baudLabel);
        layout.linkSize(SwingConstants.VERTICAL, connect, dataLabel);
        layout.linkSize(SwingConstants.VERTICAL, connect, stopBitsLabel);
        layout.linkSize(SwingConstants.VERTICAL, connect, parityLabel);

        layout.linkSize(SwingConstants.VERTICAL, disconnect, comBox);
        layout.linkSize(SwingConstants.VERTICAL, disconnect, baudComboBox);
        layout.linkSize(SwingConstants.VERTICAL, disconnect, dataComboBox);
        layout.linkSize(SwingConstants.VERTICAL, disconnect, stopBitsComboBox);
        layout.linkSize(SwingConstants.VERTICAL, disconnect, parityComboBox);


        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
          .addGroup(layout.createSequentialGroup()
            .addGap(0, 0, 100)
            .addComponent(startCOM)
            .addComponent(stopCOM)
            .addGap(25, 400, 1000)
            .addComponent(save)
            .addGap(0, 10, 100)
          )
          .addGroup(layout.createSequentialGroup()
            .addGap(0, 10, 100)
            .addComponent(readingStatus)
            .addGap(50, 500, 1500)
            .addComponent(fileNameSave)
            .addGap(0, 10, 100)
          )
          .addGroup(layout.createSequentialGroup()
            .addGap(0, 10, 100)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
              .addComponent(connect)
              .addComponent(disconnect)
            )
            .addGap(5, 10, 40)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
              .addComponent(comLabel)
              .addComponent(comBox)
            )
            .addGap(0, 10, 40)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
              .addComponent(baudLabel)
              .addComponent(baudComboBox)
            )
            .addGap(0, 10, 40)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
              .addComponent(dataLabel)
              .addComponent(dataComboBox)
            )
            .addGap(0, 10, 40)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
              .addComponent(stopBitsLabel)
              .addComponent(stopBitsComboBox)
            )
            .addGap(0, 10, 40)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
              .addComponent(parityLabel)
              .addComponent(parityComboBox)
            )
            .addGap(0, 10, 1000)

          )



        );


        layout.linkSize(SwingConstants.HORIZONTAL, save, fileNameSave);
        layout.linkSize(SwingConstants.HORIZONTAL, connect, disconnect);
        layout.linkSize(SwingConstants.HORIZONTAL, comBox, comLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, baudComboBox, baudLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, dataComboBox, dataLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, stopBitsComboBox, stopBitsLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, parityComboBox, parityLabel);
    }

    public void setConnect(JButton connect) {
        this.connect = connect;
    }

    public void setDisconnect(JButton disconnect) {
        this.disconnect = disconnect;
    }

    public void setComLabel(JLabel comLabel) {
        this.comLabel = comLabel;
    }

    public void setComBox(JComboBox comBox) {
        this.comBox = comBox;
    }

    public void setStartCOM(JButton startCOM) {
        this.startCOM = startCOM;
    }

    public void setStopCOM(JButton stopCOM) {
        this.stopCOM = stopCOM;
    }

    public void setReadingStatus(JLabel readingStatus) {
        this.readingStatus = readingStatus;
    }

    public void setSave(JButton save) {
        this.save = save;
    }

    public void setFileNameSave(JLabel fileNameSave) {
        this.fileNameSave = fileNameSave;
    }

    public void setBaudLabel(JLabel baudLabel) {
        this.baudLabel = baudLabel;
    }

    public void setBaudComboBox(JComboBox baudComboBox) {
        this.baudComboBox = baudComboBox;
    }

    public void setDataLabel(JLabel dataLabel) {
        this.dataLabel = dataLabel;
    }

    public void setDataComboBox(JComboBox dataComboBox) {
        this.dataComboBox = dataComboBox;
    }

    public void setStopBitsLabel(JLabel stopBitsLabel) {
        this.stopBitsLabel = stopBitsLabel;
    }

    public void setStopBitsComboBox(JComboBox stopBitsComboBox) {
        this.stopBitsComboBox = stopBitsComboBox;
    }

    public void setParityLabel(JLabel parityLabel) {
        this.parityLabel = parityLabel;
    }

    public void setParityComboBox(JComboBox parityComboBox) {
        this.parityComboBox = parityComboBox;
    }
}
