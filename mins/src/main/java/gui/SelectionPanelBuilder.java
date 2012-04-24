package gui;

import javax.swing.*;

public class SelectionPanelBuilder {
    
    private SelectionPanel selectionPanel;
    
    
    public SelectionPanelBuilder() {
        
    }
    
    public SelectionPanelBuilder aSelectionPanel() {
        selectionPanel = new SelectionPanel();
        return this;
    }

    public SelectionPanelBuilder withSlider(JSlider slider){
        selectionPanel.setSlider(slider);
        return this;
    }

    public SelectionPanelBuilder withNext(JButton next){
        selectionPanel.setNext(next);
        return this;
    }
    public SelectionPanelBuilder withBack(JButton back){
        selectionPanel.setBack(back);
        return this;
    }

    public SelectionPanelBuilder withLoadButton(JButton load) {
        selectionPanel.setLoad(load);
        return this;
    }

    public SelectionPanelBuilder withFileLoadNameLabel(JLabel fileNameLabel) {
        selectionPanel.setFileNameLoad(fileNameLabel);
        return this;
    }

    public SelectionPanelBuilder withProcessedDataRadioButton(JRadioButton processedDataRadioButton){
        selectionPanel.setProcessedDataMode(processedDataRadioButton);
        return this;
    }

    public SelectionPanelBuilder withRawDataRadioButton(JRadioButton rawDataRadioButton){
        selectionPanel.setRawDataMode(rawDataRadioButton);
        return this;
    }

    public SelectionPanelBuilder withPlayStartButton(JButton playStartButton){
        selectionPanel.setStartPlay(playStartButton);
        return this;
    }

    public SelectionPanelBuilder withPlayStopButton(JButton playStopButton) {
        selectionPanel.setStopPlay(playStopButton);
        return this;
    }

    public SelectionPanelBuilder withGoToButton(JButton goToButton) {
        selectionPanel.setGoTo(goToButton);
        return this;
    }

    public SelectionPanelBuilder withSeekEditField(JTextField seekEditField) {
        selectionPanel.setSeek(seekEditField);
        return this;
    }


    /**
     * COM Options
     */

    public SelectionPanelBuilder withCOMStartButton(JButton comStartButton) {
        selectionPanel.setStartCOM(comStartButton);
        return this;
    }

    public SelectionPanelBuilder withCOMStopButton(JButton comStopButton) {
        selectionPanel.setStopCOM(comStopButton);
        return this;
    }

    public SelectionPanelBuilder withSaveButton(JButton saveButton) {
        selectionPanel.setSave(saveButton);
        return this;
    }

    public SelectionPanelBuilder withFileSaveNameLabel(JLabel fileSaveNameLabel) {
        selectionPanel.setFileNameSave(fileSaveNameLabel);
        return this;
    }

    public SelectionPanelBuilder withReadingStatusLabel(JLabel readingStatusLabel) {
        selectionPanel.setReadingStatus(readingStatusLabel);
        return this;
    }

    public SelectionPanelBuilder withConnectButton(JButton connectButton) {
        selectionPanel.setConnect(connectButton);
        return this;
    }

    public SelectionPanelBuilder withDisconnectButton(JButton disconnectButton) {
        selectionPanel.setDisconnect(disconnectButton);
        return this;
    }

    public SelectionPanelBuilder withCOMBox(JComboBox comBox) {
        selectionPanel.setComBox(comBox);
        return this;
    }

    public SelectionPanelBuilder withBaudBox(JComboBox baudBox) {
        selectionPanel.setBaudComboBox(baudBox);
        return this;
    }

    public SelectionPanelBuilder withDataBox(JComboBox dataBox) {
        selectionPanel.setDataComboBox(dataBox);
        return this;
    }

    public SelectionPanelBuilder withStopBitsBox(JComboBox stopBitsBox) {
        selectionPanel.setStopBitsComboBox(stopBitsBox);
        return this;
    }

    public SelectionPanelBuilder withParityBox(JComboBox parityBox) {
        selectionPanel.setParityComboBox(parityBox);
        return this;
    }
    
    public SelectionPanelBuilder withButtonGroup(ButtonGroup buttonGroup){
        selectionPanel.setButtonGroup(buttonGroup);
        return this;
    }

    public SelectionPanel build() {
        selectionPanel.init();
        return selectionPanel;
    }

    
}
