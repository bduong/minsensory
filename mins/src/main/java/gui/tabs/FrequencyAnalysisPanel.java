package gui.tabs;

import org.jfree.chart.ChartPanel;

import javax.swing.*;

public class FrequencyAnalysisPanel extends JPanel {

    private JLabel saveToFileLabel;
    private JLabel saveFileSelected;
    private JButton selectSaveFileButton;

    private JLabel thresholdLabel;

    private JLabel alphaLabel;
    private JLabel betaLabel;
    private JLabel gammaLabel;
    private JLabel deltaLabel;
    private JLabel thetaLabel;

    private JFormattedTextField alphaField;
    private JFormattedTextField betaField;
    private JFormattedTextField gammaField;
    private JFormattedTextField deltaField;
    private JFormattedTextField thetaField;

    private JSlider alphaSlider;
    private JSlider betaSlider;
    private JSlider gammaSlider;
    private JSlider deltaSlider;
    private JSlider thetaSlider;

    private JButton processButton;
    private JTable bandColorTable;

    private ChartPanel frequencyChart;


    public FrequencyAnalysisPanel() {
        saveToFileLabel = new JLabel("Save To File:");
        thresholdLabel = new JLabel("Band Threshold Percentages:");

        alphaLabel = new JLabel("Alpha");
        betaLabel = new JLabel("Beta");
        gammaLabel = new JLabel("Gamma");
        deltaLabel = new JLabel("Delta");
        thetaLabel = new JLabel("Theta");

        Object [][] data = {
          {"Alpha", "8-12 Hz"},
          {"Beta", "13-30 Hz"},
          {"Delta", "1-4 Hz"},
          {"Gamma", "30-70 Hz"},
          {"Theta", "4-8 Hz"}
        };

        bandColorTable = new JTable(data,new String[]{"Band", "Freq & Color"});
    }

    public void layoutComponents() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHonorsVisibility(false);

        layout.setVerticalGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(saveToFileLabel)
            .addComponent(saveFileSelected)
            .addComponent(selectSaveFileButton)
          )
          .addGap(5, 10, 20)
          .addComponent(thresholdLabel)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(alphaLabel)
            .addComponent(betaLabel)
            .addComponent(deltaLabel)
            .addComponent(gammaLabel)
            .addComponent(thetaLabel)
          )
          .addGap(0, 5, 10)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(alphaField)
            .addComponent(betaField)
            .addComponent(deltaField)
            .addComponent(gammaField)
            .addComponent(thetaField)
          )
          .addGap(0, 5, 10)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(alphaSlider)
            .addComponent(betaSlider)
            .addComponent(deltaSlider)
            .addComponent(gammaSlider)
            .addComponent(thetaSlider)
          )
          .addGap(5, 10, 20)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addComponent(processButton)
            .addComponent(bandColorTable)
            .addComponent(frequencyChart)
          )

        );

        layout.linkSize(SwingConstants.VERTICAL, saveToFileLabel, saveFileSelected);
        layout.linkSize(SwingConstants.VERTICAL, saveToFileLabel, selectSaveFileButton);

        layout.linkSize(SwingConstants.VERTICAL, alphaLabel, betaLabel);
        layout.linkSize(SwingConstants.VERTICAL, alphaLabel, deltaLabel);
        layout.linkSize(SwingConstants.VERTICAL, alphaLabel, gammaLabel);
        layout.linkSize(SwingConstants.VERTICAL, alphaLabel, thetaLabel);

        layout.linkSize(SwingConstants.VERTICAL, alphaField, betaField);
        layout.linkSize(SwingConstants.VERTICAL, alphaField, deltaField);
        layout.linkSize(SwingConstants.VERTICAL, alphaField, gammaField);
        layout.linkSize(SwingConstants.VERTICAL, alphaField, thetaField);

        layout.linkSize(SwingConstants.VERTICAL, alphaSlider, betaSlider);
        layout.linkSize(SwingConstants.VERTICAL, alphaSlider, deltaSlider);
        layout.linkSize(SwingConstants.VERTICAL, alphaSlider, gammaSlider);
        layout.linkSize(SwingConstants.VERTICAL, alphaSlider, thetaSlider);


        layout.setHorizontalGroup(
          layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
              .addComponent(saveToFileLabel)
              .addComponent(saveFileSelected)
              .addComponent(selectSaveFileButton)
            )
            .addComponent(thresholdLabel)
            .addGroup(layout.createSequentialGroup()
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(alphaLabel)
                .addComponent(alphaField)
                .addComponent(alphaSlider)
              )
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(betaLabel)
                .addComponent(betaField)
                .addComponent(betaSlider)
              )
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(deltaLabel)
                .addComponent(deltaField)
                .addComponent(deltaSlider)
              )
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(gammaLabel)
                .addComponent(gammaField)
                .addComponent(gammaSlider)
              )
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(thetaLabel)
                .addComponent(thetaField)
                .addComponent(thetaSlider)
              )
            )
            .addGroup(layout.createSequentialGroup()
              .addComponent(processButton)
              .addComponent(bandColorTable)
              .addComponent(frequencyChart)
            )

        );

        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, alphaField);
        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, alphaSlider);

        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, betaLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, betaField);
        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, betaSlider);

        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, deltaLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, deltaField);
        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, deltaSlider);

        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, gammaLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, gammaField);
        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, gammaSlider);

        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, thetaLabel);
        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, thetaField);
        layout.linkSize(SwingConstants.HORIZONTAL, alphaLabel, thetaSlider);

    }

    public void setSaveFileSelected(JLabel saveFileSelected) {
        this.saveFileSelected = saveFileSelected;
    }

    public void setSelectSaveFileButton(JButton selectSaveFileButton) {
        this.selectSaveFileButton = selectSaveFileButton;
    }

    public void setAlphaField(JFormattedTextField alphaField) {
        this.alphaField = alphaField;
    }

    public void setBetaField(JFormattedTextField betaField) {
        this.betaField = betaField;
    }

    public void setGammaField(JFormattedTextField gammaField) {
        this.gammaField = gammaField;
    }

    public void setThetaField(JFormattedTextField thetaField) {
        this.thetaField = thetaField;
    }

    public void setDeltaField(JFormattedTextField deltaField) {
        this.deltaField = deltaField;
    }

    public void setAlphaSlider(JSlider alphaSlider) {
        this.alphaSlider = alphaSlider;
    }

    public void setBetaSlider(JSlider betaSlider) {
        this.betaSlider = betaSlider;
    }

    public void setGammaSlider(JSlider gammaSlider) {
        this.gammaSlider = gammaSlider;
    }

    public void setDeltaSlider(JSlider deltaSlider) {
        this.deltaSlider = deltaSlider;
    }

    public void setThetaSlider(JSlider thetaSlider) {
        this.thetaSlider = thetaSlider;
    }

    public void setProcessButton(JButton processButton) {
        this.processButton = processButton;
    }

    public void setBandColorTable(JTable bandColorTable) {
        this.bandColorTable = bandColorTable;
    }

    public void setFrequencyChart(ChartPanel frequencyChart) {
        this.frequencyChart = frequencyChart;
    }
}
