package gui;

import gui.tabs.FrequencyAnalysisPanel;
import gui.tabs.PlayBackPanel;
import gui.tabs.RealTimePanel;
import gui.tabs.SDCapturePanel;

import javax.swing.*;

/**
 * The <code>SelectionPanel</code> object is used to build the configuration panels for the user to select
 * different options.
 *
 * The objects are not actually made in this object. They are simply put into a layout.
 * The actually references to the object need to be set through the setters.
 */
public class SelectionPanel extends JTabbedPane{

    private PlayBackPanel playBackOptions;
    private RealTimePanel realTimeOptions;
    private SDCapturePanel sdCardOptions;
    private FrequencyAnalysisPanel frequencyAnalysisOptions;

    public SelectionPanel() {
        playBackOptions = new PlayBackPanel();
        realTimeOptions = new RealTimePanel();
        sdCardOptions = new SDCapturePanel();
        frequencyAnalysisOptions = new FrequencyAnalysisPanel();
    }

    /**
     * SD Card Reader Options
     */

    public void init() {
        playBackOptions.layoutComponents();
        realTimeOptions.layoutComponents();
        sdCardOptions.layoutComponents();
        frequencyAnalysisOptions.layoutComponents();

        addTab("Play Back", null, playBackOptions, "Play back data from File");
        addTab("Real Time", null, realTimeOptions, "Record Data From USB");
        addTab("SD Capture", null, sdCardOptions, "Capture Data From SD Card");
        addTab("Freq Analysis", null, frequencyAnalysisOptions, "Do Frequency Analysis");
    }

    public PlayBackPanel getPlayBackOptions() {
        return playBackOptions;
    }

    public RealTimePanel getRealTimeOptions() {
        return realTimeOptions;
    }

    public SDCapturePanel getSDCardOptions() {
        return sdCardOptions;
    }

    public FrequencyAnalysisPanel getFrequencyAnalysisOptions(){
        return frequencyAnalysisOptions;
    }
}
