/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package com.minsensory.gui;

import com.minsensory.gui.tabs.FrequencyAnalysisPanel;
import com.minsensory.gui.tabs.PlayBackPanel;
import com.minsensory.gui.tabs.RealTimePanel;
import com.minsensory.gui.tabs.SDCapturePanel;
import org.jfree.chart.ChartPanel;

import javax.swing.*;

/**
 * The <code>SelectionPanelBuilder</code> is a builder utility class that allows for
 * quick assembly of the SelectionPanel.
 *
 * It is used to build the selection panel in the user interface while keeping the setter calls
 * organized.
 */
public class SelectionPanelBuilder {
    
    private SelectionPanel selectionPanel;
    private PlayBackPanel playBackPanel;
    private RealTimePanel realTimePanel;
    private SDCapturePanel sdCapturePanel;
    private FrequencyAnalysisPanel frequencyAnalysisPanel;
    
    
    public SelectionPanelBuilder() {
        
    }
    
    public SelectionPanelBuilder aSelectionPanel() {
        selectionPanel = new SelectionPanel();
        playBackPanel = selectionPanel.getPlayBackOptions();
        realTimePanel = selectionPanel.getRealTimeOptions();
        sdCapturePanel = selectionPanel.getSDCardOptions();
        frequencyAnalysisPanel = selectionPanel.getFrequencyAnalysisOptions();
        return this;
    }

    /**
     * Play Back Options
     *
     */
    public SelectionPanelBuilder withSlider(JSlider slider){
        playBackPanel.setSlider(slider);
        return this;
    }

    public SelectionPanelBuilder withNext(JButton next){
        playBackPanel.setNext(next);
        return this;
    }

    public SelectionPanelBuilder withBack(JButton back){
        playBackPanel.setBack(back);
        return this;
    }

    public SelectionPanelBuilder withLoadButton(JButton load) {
        playBackPanel.setLoad(load);
        return this;
    }

    public SelectionPanelBuilder withFileLoadNameLabel(JLabel fileNameLabel) {
        playBackPanel.setFileNameLoad(fileNameLabel);
        return this;
    }

    public SelectionPanelBuilder withProcessedDataRadioButton(JRadioButton processedDataRadioButton){
        playBackPanel.setProcessedDataMode(processedDataRadioButton);
        return this;
    }

    public SelectionPanelBuilder withRawDataRadioButton(JRadioButton rawDataRadioButton){
        playBackPanel.setRawDataMode(rawDataRadioButton);
        return this;
    }

    public SelectionPanelBuilder withPlayStartButton(JButton playStartButton){
        playBackPanel.setStartPlay(playStartButton);
        return this;
    }

    public SelectionPanelBuilder withPlayStopButton(JButton playStopButton) {
        playBackPanel.setStopPlay(playStopButton);
        return this;
    }

    public SelectionPanelBuilder withGoToButton(JButton goToButton) {
        playBackPanel.setGoTo(goToButton);
        return this;
    }

    public SelectionPanelBuilder withSeekEditField(JTextField seekEditField) {
        playBackPanel.setSeek(seekEditField);
        return this;
    }

    /**
     * Real Time Options
     */

    public SelectionPanelBuilder withRealTimeSaveButton(JButton saveButton) {
        realTimePanel.setSave(saveButton);
        return this;
    }

    public SelectionPanelBuilder withFileSaveNameLabel(JLabel fileSaveNameLabel) {
        realTimePanel.setFileNameSave(fileSaveNameLabel);
        return this;
    }

    public SelectionPanelBuilder withCOMStartButton(JButton comStartButton) {
        realTimePanel.setStartCOM(comStartButton);
        return this;
    }

    public SelectionPanelBuilder withCOMStopButton(JButton comStopButton) {
        realTimePanel.setStopCOM(comStopButton);
        return this;
    }

    public SelectionPanelBuilder withReadingStatusLabel(JLabel readingStatusLabel) {
        realTimePanel.setReadingStatus(readingStatusLabel);
        return this;
    }

    public SelectionPanelBuilder withRealTimeCOMBox(JComboBox comBox) {
        realTimePanel.setComBox(comBox);
        return this;
    }

    public SelectionPanelBuilder withRealTimeBaudBox(JComboBox baudBox) {
        realTimePanel.setBaudComboBox(baudBox);
        return this;
    }

    public SelectionPanelBuilder withRealTimeDataBox(JComboBox dataBox) {
        realTimePanel.setDataComboBox(dataBox);
        return this;
    }

    public SelectionPanelBuilder withRealTimeStopBitsBox(JComboBox stopBitsBox) {
        realTimePanel.setStopBitsComboBox(stopBitsBox);
        return this;
    }

    public SelectionPanelBuilder withRealTimeParityBox(JComboBox parityBox) {
        realTimePanel.setParityComboBox(parityBox);
        return this;
    }

    public SelectionPanelBuilder withConnectButton(JButton connectButton) {
        realTimePanel.setConnect(connectButton);
        return this;
    }

    public SelectionPanelBuilder withDisconnectButton(JButton disconnectButton) {
        realTimePanel.setDisconnect(disconnectButton);
        return this;
    }


    /**
     * SD Card Options
     */

    public SelectionPanelBuilder withSDFileSaveName(JLabel saveNameLabel) {
        sdCapturePanel.setSaveFileSelected(saveNameLabel);
        return this;
    }

    public SelectionPanelBuilder withSDSaveButton(JButton saveButton) {
        sdCapturePanel.setSelectSaveFileButton(saveButton);
        return this;
    }

    public SelectionPanelBuilder withSDCOMBox(JComboBox comboBox) {
        sdCapturePanel.setComBox(comboBox);
        return this;
    }

    public SelectionPanelBuilder withSDBaudBox(JComboBox baudBox) {
        sdCapturePanel.setBaudComboBox(baudBox);
        return this;
    }

    public SelectionPanelBuilder withSDDataBox(JComboBox dataBox) {
        sdCapturePanel.setDataComboBox(dataBox);
        return this;
    }

    public SelectionPanelBuilder withSDStopBitsBox(JComboBox stopBitsBox) {
        sdCapturePanel.setStopBitsComboBox(stopBitsBox);
        return this;
    }

    public SelectionPanelBuilder withSDParityBox(JComboBox parityBox) {
        sdCapturePanel.setParityComboBox(parityBox);
        return this;
    }

    public SelectionPanelBuilder withStartTransferButton(JButton startTransfer) {
        sdCapturePanel.setStartTransfer(startTransfer);
        return this;
    }

    public SelectionPanelBuilder withStopTransferButton(JButton stopTransfer) {
        sdCapturePanel.setStopTransfer(stopTransfer);
        return this;
    }

    public SelectionPanelBuilder withBytesTransferredLabel(JLabel bytesTransferred) {
        sdCapturePanel.setBytesTransferred(bytesTransferred);
        return this;
    }

    /**
     * Frequency Analysis Options
     */

    public SelectionPanelBuilder withFrequencySaveFileLabel(JLabel saveFileLabel) {
        frequencyAnalysisPanel.setSaveFileSelected(saveFileLabel);
        return this;
    }

    public SelectionPanelBuilder withFrequencySaveButton(JButton saveButton) {
        frequencyAnalysisPanel.setSelectSaveFileButton(saveButton);
        return this;
    }

    public SelectionPanelBuilder withAlphaField(JFormattedTextField alphaField) {
        frequencyAnalysisPanel.setAlphaField(alphaField);
        return this;
    }

    public SelectionPanelBuilder withBetaField(JFormattedTextField betaField) {
        frequencyAnalysisPanel.setBetaField(betaField);
        return this;
    }

    public SelectionPanelBuilder withGammaField(JFormattedTextField gammaField){
        frequencyAnalysisPanel.setGammaField(gammaField);
        return this;
    }

    public SelectionPanelBuilder withDeltaField(JFormattedTextField deltaField) {
        frequencyAnalysisPanel.setDeltaField(deltaField);
        return this;
    }

    public SelectionPanelBuilder withThetaField(JFormattedTextField thetaField) {
        frequencyAnalysisPanel.setThetaField(thetaField);
        return this;
    }

    public SelectionPanelBuilder withAlphaSlider(JSlider alphaSlider) {
        frequencyAnalysisPanel.setAlphaSlider(alphaSlider);
        return this;
    }

    public SelectionPanelBuilder withBetaSlider(JSlider betaSlider){
        frequencyAnalysisPanel.setBetaSlider(betaSlider);
        return this;
    }

    public SelectionPanelBuilder withDeltaSlider(JSlider deltaSlider) {
        frequencyAnalysisPanel.setDeltaSlider(deltaSlider);
        return this;
    }

    public SelectionPanelBuilder withGammaSlider(JSlider gammaSlider) {
        frequencyAnalysisPanel.setGammaSlider(gammaSlider);
        return this;
    }

    public SelectionPanelBuilder withThetaSlider(JSlider thetaSlider) {
        frequencyAnalysisPanel.setThetaSlider(thetaSlider);
        return this;
    }

    public SelectionPanelBuilder withProcessButton(JButton processButton) {
        frequencyAnalysisPanel.setProcessButton(processButton);
        return this;
    }

    public SelectionPanelBuilder withFrequencyChart(ChartPanel frequencyChart) {
        frequencyAnalysisPanel.setFrequencyChart(frequencyChart);
        return this;
    }

    public SelectionPanel build() {
        selectionPanel.init();
        return selectionPanel;
    }

    
}
