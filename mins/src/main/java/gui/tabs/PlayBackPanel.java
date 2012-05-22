package gui.tabs;

import javax.swing.*;

public class PlayBackPanel extends JPanel{

    private JPanel playOptions;
    private JPanel comOptions;

    private JSlider slider;
    private JButton back;
    private JButton next;

    private JButton load;
    private JLabel fileNameLoad;

    private JRadioButton processedDataMode;

    private JRadioButton rawDataMode;

    private JButton startPlay;
    private JButton stopPlay;

    private JTextField seek;
    private JButton goTo;


    public PlayBackPanel() {}

    public void layoutComponents(){
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHonorsVisibility(false);

        layout.setVerticalGroup(layout.createSequentialGroup()
          .addGap(0, 5, 100)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(goTo)
            .addComponent(seek)
          )
          .addGap(0, 5, 100)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(back)
            .addComponent(slider)
            .addComponent(next)
          )
          .addGap(0, 20, 100)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(load)
            .addComponent(processedDataMode)
            .addComponent(startPlay)
          )
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(fileNameLoad)
            .addComponent(rawDataMode)
            .addComponent(stopPlay)
          )
          .addGap(0, 5, 100)
        );


        layout.linkSize(SwingConstants.VERTICAL, back, slider);
        layout.linkSize(SwingConstants.VERTICAL, back, next);
        layout.linkSize(SwingConstants.VERTICAL, goTo, seek);
        layout.linkSize(SwingConstants.VERTICAL, load, processedDataMode);
        layout.linkSize(SwingConstants.VERTICAL, load, startPlay);
        layout.linkSize(SwingConstants.VERTICAL, fileNameLoad, rawDataMode);
        layout.linkSize(SwingConstants.VERTICAL, fileNameLoad, stopPlay);



        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
          .addGroup(layout.createSequentialGroup()
            .addGap(0, 50, 100)
            .addComponent(goTo)
            .addComponent(seek)
            .addGap(0, 50, 100)
          )
          .addGroup(layout.createSequentialGroup()
            .addComponent(back)
            .addComponent(slider)
            .addComponent(next)
          )
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
              .addGap(0, 50, 100)
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(load)
                .addComponent(fileNameLoad)
              )
              .addGap(0, 5, 50)
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(processedDataMode)
                .addComponent(rawDataMode)
              )
              .addGap(0, 5, 50)
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(startPlay)
                .addComponent(stopPlay)
              )
              .addGap(0, 50, 100)
            )
          ));

        layout.linkSize(SwingConstants.HORIZONTAL, load, fileNameLoad);
        layout.linkSize(SwingConstants.HORIZONTAL, rawDataMode, processedDataMode);
        layout.linkSize(SwingConstants.HORIZONTAL, startPlay, stopPlay);
    }

    public void setPlayOptions(JPanel playOptions) {
        this.playOptions = playOptions;
    }

    public void setComOptions(JPanel comOptions) {
        this.comOptions = comOptions;
    }

    public void setSlider(JSlider slider) {
        this.slider = slider;
    }

    public void setBack(JButton back) {
        this.back = back;
    }

    public void setNext(JButton next) {
        this.next = next;
    }

    public void setLoad(JButton load) {
        this.load = load;
    }

    public void setFileNameLoad(JLabel fileNameLoad) {
        this.fileNameLoad = fileNameLoad;
    }

    public void setProcessedDataMode(JRadioButton processedDataMode) {
        this.processedDataMode = processedDataMode;
    }

    public void setRawDataMode(JRadioButton rawDataMode) {
        this.rawDataMode = rawDataMode;
    }

    public void setStartPlay(JButton startPlay) {
        this.startPlay = startPlay;
    }

    public void setStopPlay(JButton stopPlay) {
        this.stopPlay = stopPlay;
    }

    public void setSeek(JTextField seek) {
        this.seek = seek;
    }

    public void setGoTo(JButton goTo) {
        this.goTo = goTo;
    }
}
