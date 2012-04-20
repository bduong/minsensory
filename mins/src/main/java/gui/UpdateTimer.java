package gui;

import data.DataBank;
import data.DataLine;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class UpdateTimer implements ActionListener{

    private int delay;
    private Timer timer;
    private ColorMappedImage image;
    private PlotPanel plotPanel;
    private DataBank dataBank;
    private Set<Integer> spikeNodes;
    private Map<Integer, Integer> spikeTime;

    private UI ui;

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
        initMap();
    }

    public void setApplication(UI ui){
        this.ui = ui;
    }

    private void setupTimer() {
        spikeNodes = new HashSet<Integer>();
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

    private void initMap() {
        spikeTime = new HashMap<Integer, Integer>();
        for (int ii = 0; ii < 256; ii++){
            spikeTime.put(ii, 0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!dataBank.isAtEnd()){
            DataLine data = dataBank.getNextPoint();
            plotPanel.updatePlots(data);
            List<Integer> spikes = image.updateImage(data, spikeNodes);
            spikeNodes.addAll(spikes);

            for (int key : spikeTime.keySet()) {
                int time = spikeTime.get(key);
                if (time > 0) {
                    spikeTime.put(key, time-1);
                }
            }

            for (int node : spikes) {
                spikeTime.put(node, 30);
            }

            for (int node : spikeNodes) {
                if (spikeTime.get(node) <= 0) {
                    spikeNodes.remove(node);
                }
            }

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
