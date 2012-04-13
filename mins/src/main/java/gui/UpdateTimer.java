package gui;

import data.DataBank;
import data.DataLine;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateTimer implements ActionListener{

    private int delay;
    private Timer timer;
    private ColorMappedImage image;
    private PlotPanel plotPanel;
    private DataBank dataBank;
    private int count;

    private UI ui;

    private final int NUMBER_OF_POINTS = 10000;
    
    private JSlider slider;

    public UpdateTimer() {
        delay = 1000/30;
        setupTimer();
    }

    public UpdateTimer(int delay){
        this.delay = delay;
        setupTimer();
    }

    public UpdateTimer(int delay, ColorMappedImage image, PlotPanel plotPanel, DataBank dataBank) {
        slider = null;

        this.delay = delay;
        this.image = image;
        this.plotPanel = plotPanel;
        this.dataBank = dataBank;
        setupTimer();
    }

    public void setApplication(UI ui){
        this.ui = ui;
    }

    private void setupTimer() {
        count = 0;
        timer = new Timer(delay, this);
    }

    public void startTimer() {
        if (!dataBank.isAtEnd()){
            timer.start();
        } else {
            ui.reportFinish();
        }
    }

    public void stopTimer() {
        timer.stop();
    }

    public void setSlider(JSlider slider){
        this.slider = slider;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!dataBank.isAtEnd()){
            DataLine data = dataBank.getNextPoint();
            plotPanel.updatePlots(data);
            image.updateImage(data);

            if(slider != null) {
                ui.setUserSeek(false);
                slider.setValue(slider.getValue()+1);
            }

        } else {
            stopTimer();
            ui.reportFinish();
        }
    }
}
