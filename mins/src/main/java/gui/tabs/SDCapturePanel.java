package gui.tabs;

import javax.swing.*;

public class SDCapturePanel extends JPanel {

    private JLabel saveToFileLabel;
    private JButton selectSaveFileButton;
    private JLabel saveFileSelected;

    private JLabel comLabel;
    private JComboBox comBox;

    private JLabel baudLabel;
    private JComboBox baudComboBox;

    private JLabel dataLabel;
    private JComboBox dataComboBox;

    private JLabel stopBitsLabel;
    private JComboBox stopBitsComboBox;

    private JLabel parityLabel;
    private JComboBox parityComboBox;

    private JButton startTransfer;
    private JButton stopTransfer;

    private JLabel bytesTransferLabel;
    private JLabel bytesTransferred;

    public SDCapturePanel(){
        saveToFileLabel = new JLabel("Save To File:");
        comLabel = new JLabel("COM");
        baudLabel = new JLabel("BAUD");
        dataLabel = new JLabel("DataBits");
        stopBitsLabel = new JLabel("Stop Bits");
        parityLabel = new JLabel("Parity Bit");
        bytesTransferLabel = new JLabel("Bytes Transferred:");


    }

    public void layoutComponents(){
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);


        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHonorsVisibility(false);


        layout.setVerticalGroup(layout.createSequentialGroup()
          .addGap(0, 5, 100)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(saveToFileLabel)
            .addComponent(saveFileSelected)
            .addComponent(selectSaveFileButton)
          )
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(comLabel)
            .addComponent(comBox)
          )
          .addGap(5, 10, 50)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(baudLabel)
            .addComponent(dataLabel)
            .addComponent(stopBitsLabel)
            .addComponent(parityLabel)
          )
          .addGap(0, 5, 10)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(baudComboBox)
            .addComponent(dataComboBox)
            .addComponent(stopBitsComboBox)
            .addComponent(parityComboBox)
          )
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(startTransfer)
            .addComponent(stopTransfer)
            .addComponent(bytesTransferLabel)
            .addComponent(bytesTransferred)
          )
        );

        layout.linkSize(SwingConstants.VERTICAL, saveToFileLabel, saveFileSelected);
        layout.linkSize(SwingConstants.VERTICAL, saveToFileLabel, selectSaveFileButton);

        layout.linkSize(SwingConstants.VERTICAL, comLabel, comBox);

        layout.linkSize(SwingConstants.VERTICAL, baudLabel, dataLabel);
        layout.linkSize(SwingConstants.VERTICAL, baudLabel, stopBitsLabel);
        layout.linkSize(SwingConstants.VERTICAL, baudLabel, parityLabel);

        layout.linkSize(SwingConstants.VERTICAL, baudComboBox, dataComboBox);
        layout.linkSize(SwingConstants.VERTICAL, baudComboBox, stopBitsComboBox);
        layout.linkSize(SwingConstants.VERTICAL, baudComboBox, parityComboBox);


        layout.linkSize(SwingConstants.VERTICAL, startTransfer, stopTransfer);
        layout.linkSize(SwingConstants.VERTICAL, startTransfer, bytesTransferLabel);
        layout.linkSize(SwingConstants.VERTICAL, startTransfer, bytesTransferred);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(saveToFileLabel)
            .addComponent(saveFileSelected)
            .addGap(5,10,20)
            .addComponent(selectSaveFileButton)
          )
          .addGroup(layout.createSequentialGroup()
            .addComponent(comLabel)
            .addGap(0,5,10)
            .addComponent(comBox)
            .addGap(10, 50, 200)
          )
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
              .addComponent(baudLabel)
              .addComponent(baudComboBox)
            )
            .addGap(0, 5, 10)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)

              .addComponent(dataLabel)
              .addComponent(dataComboBox)
            )
            .addGap(0, 5, 10)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
              .addComponent(stopBitsLabel)
              .addComponent(stopBitsComboBox)
            )
            .addGap(0,5,10)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
              .addComponent(parityLabel)
              .addComponent(parityComboBox)
            )
          )

          .addGroup(layout.createSequentialGroup()
            .addComponent(startTransfer)
            .addComponent(stopTransfer)
            .addGap(5,10,20)
            .addComponent(bytesTransferLabel)
            .addComponent(bytesTransferred)
          )
        );

        //layout.linkSize(SwingConstants.HORIZONTAL, comLabel, baudLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, baudLabel, baudComboBox);
        layout.linkSize(SwingConstants.HORIZONTAL, baudLabel, comBox);
        layout.linkSize(SwingConstants.HORIZONTAL, baudLabel, dataLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, baudLabel, dataComboBox);
        layout.linkSize(SwingConstants.HORIZONTAL, baudLabel, stopBitsLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, baudLabel, stopBitsComboBox);
        layout.linkSize(SwingConstants.HORIZONTAL, baudLabel, parityLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, baudLabel, parityComboBox);


    }

    public void setSelectSaveFileButton(JButton selectSaveFileButton) {
        this.selectSaveFileButton = selectSaveFileButton;
    }

    public void setSaveFileSelected(JLabel saveFileSelected) {
        this.saveFileSelected = saveFileSelected;
    }

    public void setComBox(JComboBox comBox) {
        this.comBox = comBox;
    }

    public void setBaudComboBox(JComboBox baudComboBox) {
        this.baudComboBox = baudComboBox;
    }

    public void setDataComboBox(JComboBox dataComboBox) {
        this.dataComboBox = dataComboBox;
    }

    public void setStopBitsComboBox(JComboBox stopBitsComboBox) {
        this.stopBitsComboBox = stopBitsComboBox;
    }

    public void setParityComboBox(JComboBox parityComboBox) {
        this.parityComboBox = parityComboBox;
    }

    public void setStartTransfer(JButton startTransfer) {
        this.startTransfer = startTransfer;
    }

    public void setStopTransfer(JButton stopTransfer) {
        this.stopTransfer = stopTransfer;
    }

    public void setBytesTransferred(JLabel bytesTransferred) {
        this.bytesTransferred = bytesTransferred;
    }
}
