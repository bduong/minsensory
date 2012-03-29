package gui;

import data.DataBank;
import data.DataLine;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class UpdateTimer implements ActionListener{

    private int delay;
    private Timer timer;
    private ColorMappedImage image;
    private PlotPanel plotPanel;
    private DataBank dataBank;
    private int count;

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

    private void setupTimer() {
        count = 0;
        timer = new Timer(delay, this);
    }

    public void startTimer() {
        if (count < 10000){
            timer.start();
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
        System.out.println(count);
        if (count++ < 10000){
            DataLine data = dataBank.getNextPoint();
            plotPanel.updatePlots(data);
            image.updateImage(data);

            if(slider != null) {
                slider.setValue(slider.getValue()+1);
            }

        } else {
            stopTimer();
            System.out.println("DONE");
        }
    }
}
