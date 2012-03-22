package gui;

import data.DataBank;
import data.DataLine;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class UpdateTimer implements ActionListener{

    private int delay;
    private Timer timer;
    private ColorMappedImage image;
    private PlotPanel plotPanel;
    private DataBank dataBank;
    private int count;

    public UpdateTimer() {
        delay = 1000/30;
        setupTimer();
    }

    public UpdateTimer(int delay){
        this.delay = delay;
        setupTimer();
    }

    public UpdateTimer(int delay, ColorMappedImage image, PlotPanel plotPanel, DataBank dataBank) {
        this.delay = delay;
        this.image = image;
        this.plotPanel = plotPanel;
        this.dataBank = dataBank;
        setupTimer();
    }

    private void setupTimer() {
        timer = new Timer(delay, this);
    }

    public void startTimer() {
        count = 0;
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(count);
        if (count++ < 10000){
            DataLine data = dataBank.getNextPoint();
            plotPanel.updatePlots(data);
            image.updateImage(data);
        } else {
            stopTimer();
            System.out.println("DONE");
        }
}
}
