package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: bduong Date: 1/9/12 Time: 3:46 PM To change
 * this template use File | Settings | File Templates.
 */
public class PlotPanel extends JPanel implements ActionListener{

    private List<JFreeChart> plots;
    private List<XYSeries> series;
    private List<JRadioButton> radioButtons;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private int node;
    
    public PlotPanel() {
        plots = new ArrayList<JFreeChart>();
        series = new ArrayList<XYSeries>();
        radioButtons = new ArrayList<JRadioButton>();
        node = 0;
        init();
    }

    private void init() {
        //GridLayout layout = new GridLayout(5,2);
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        for (String title : new String[] {"Node 1", "Node 2", "Node 5", "Node 4", "Node 5"})    {
        plots.add(createSeriesAndChart(title));
        }

        JPanel buttonPanel = new JPanel(new GridLayout(5,1));

        JPanel chartPanel = new JPanel(new GridLayout(5,1,0,5));
        int chartNumber = 0;
        for (JFreeChart chart : plots) {
            JRadioButton button = new JRadioButton();
            button.setActionCommand("" + chartNumber++);
            chart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            ChartPanel thisChart = new ChartPanel(chart);
            radioButtons.add(button);
            buttonGroup.add(button);

            buttonPanel.add(button);
            chartPanel.add(thisChart);
        }

        radioButtons.get(0).setSelected(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
          .addComponent(buttonPanel)
          .addGap(20)
          .addComponent(chartPanel)
        );

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
          .addComponent(buttonPanel)
        .addComponent(chartPanel));

    }

    private JFreeChart createSeriesAndChart(String title) {
        XYSeries xySeries = new XYSeries(title);
        for (int ii = -49; ii <= 0; ii++ ) {
            xySeries.add(ii, ii+49);
        }
        series.add(xySeries);
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection(xySeries);
        return ChartFactory.createXYLineChart(
          title,
          "Time",
          "Voltage (mV)",
          xySeriesCollection,
          PlotOrientation.VERTICAL,
          false,
          true,
          false
        );
    }

    public void run(){
        setVisible(true);
    }

    public void changePlot(int nodeToPlot){
        JFreeChart chart = plots.get(node);
        chart.setTitle("Node " + nodeToPlot);
        radioButtons.get(node).setSelected(false);
        if(++node > 4) {
            node = 0;
        }
        radioButtons.get(node).setSelected(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        node = Integer.parseInt(actionEvent.getActionCommand());

    }
}
