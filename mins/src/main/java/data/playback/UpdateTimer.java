package data.playback;

import data.DataBank;
import data.DataLine;
import gui.ColorMappedImage;
import gui.PlotPanel;
import gui.UI;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * The <code>UpdateTimer</code> object is used to refresh the image and plots
 * on the screen when in data play back mode.
 *
 * It calls into a given <code>ColorMappedImage</code> and <code>PlotPanel</code>
 * every 1/30 of a second to pass on the next data point from the <code>DataBank</code>
 *
 */
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

    /**
     * Creates a new <code>UpdateTimer</code> linked to a given image, databank, and set of plots
     * that updates at a given delay
     *
     * @param delay delay to update at in ms
     * @param image color mapped image to update
     * @param plotPanel plots to update
     * @param dataBank data bank to get data from
     */
    public UpdateTimer(int delay, ColorMappedImage image, PlotPanel plotPanel, DataBank dataBank) {
        slider = null;

        this.delay = delay;
        this.image = image;
        this.plotPanel = plotPanel;
        this.dataBank = dataBank;
        setupTimer();
        initMap();
    }

    /**
     * Set the application to update to.
     *
     * @param ui the ui
     */
    public void setApplication(UI ui){
        this.ui = ui;
    }

    /**
     * Set up the timer.
     */
    private void setupTimer() {
        spikeNodes = new HashSet<Integer>();
        timer = new Timer(delay, this);
    }

    /**
     * Start the timer if the databank is not at the end
     */
    public void startTimer() {
        if (!dataBank.isAtEnd()){
            timer.start();
        } else {
            ui.reportFinish();
        }
    }

    /**
     * Stop the timer.
     */
    public void stopTimer() {
        timer.stop();
    }

    /**
     * Set the Slider to update.
     * @param slider
     */
    public void setSlider(JSlider slider){
        this.slider = slider;
    }

    /**
     * Initialize the hash map to keep track to of the time to flash a spiking node white.
     */
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
