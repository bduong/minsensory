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
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: bduong Date: 1/9/12 Time: 3:46 PM To change
 * this template use File | Settings | File Templates.
 */
public class PlotPanel extends JPanel {

    private List<JFreeChart> plots;
    private ButtonGroup buttonGroup = new ButtonGroup();
    
    public PlotPanel() {
        plots = new ArrayList<JFreeChart>();
        init();
    }

    private void init() {
        //GridLayout layout = new GridLayout(5,2);
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        for (String title : new String[] {"Node 1", "Node 2", "Node 5", "Node 4", "Node 5"})    {
        plots.add(createSeriesAndChart(title));
        }

        JPanel buttons = new JPanel(new GridLayout(5,1));

        JPanel charts = new JPanel(new GridLayout(5,1,0,5));
        for (JFreeChart chart : plots) {
            JRadioButton button = new JRadioButton();
            chart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            ChartPanel chartPanel = new ChartPanel(chart);
            buttonGroup.add(button);

            buttons.add(button);
            charts.add(chartPanel);
        }
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
          .addComponent(buttons)
          .addGap(20)
          .addComponent(charts)
        );

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
          .addComponent(buttons)
        .addComponent(charts));

    }

    private JFreeChart createSeriesAndChart(String title) {
        XYSeries xySeries = new XYSeries(title);
        for (int ii = -49; ii <= 0; ii++ ) {
            xySeries.add(ii, ii+49);
        }

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
}
