package gui;

import data.DataLine;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * The <code>PlotPanel</code> object is used to display the 5 voltage-time plots
 * in the user interface.
 *
 * It uses JFreeChart to render the charts.
 *
 * It keeps track of each series independently and periodically data is passed to the <code>PlotPanel</code>
 * to do an update. During an update, we take the data from the nodes that we want to display and add
 * them to the series. Then we rebuild the plots and display them.
 */
public class PlotPanel extends JPanel implements ActionListener{

    private List<JFreeChart> plots;
    private List<XYSeries> series;
    private List<JRadioButton> radioButtons;
    private List<LinkedList<Double>> dataSeries;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private int [] plotNodes;
    private int node;
    private int flashCount;
    private int flashPlot;

    private static Color colors[] = {Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.MAGENTA};

    private static char [] nodes ="ABCDEFGHIJKLMNOP".toCharArray();

    private DataType dataType;

    public PlotPanel() {
        plots = new ArrayList<JFreeChart>();
        series = new ArrayList<XYSeries>();
        radioButtons = new ArrayList<JRadioButton>();
        plotNodes = new int[]{1, 2, 3, 4, 5};
        dataSeries = new ArrayList<LinkedList<Double>>(5);
        flashCount = 0;
        flashPlot = -1;
        node = 0;
        init();
    }

    /**
     * Initialize the Charts and Plots
     */
    private void init() {

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        int colorNum = 0;
        for (String title : new String[] {"Node 1-A", "Node 1-B", "Node 1-C", "Node 1-D", "Node 1-E"})    {
            JFreeChart chart = createSeriesAndChart(title);
            chart.getXYPlot().getRenderer().setSeriesPaint(0, colors[colorNum++]);
            plots.add(chart);
        }

        JPanel chartPanel = new JPanel(new GridLayout(5,1,0,5));

        JPanel buttonPanel = new JPanel(new GridLayout(5,1));

        int chartNumber = 0;
        for (JFreeChart chart : plots) {
            JRadioButton button = new JRadioButton();
            button.setHorizontalAlignment(JRadioButton.LEFT);
            button.setActionCommand("" + chartNumber++);
            button.addActionListener(this);
            NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
            rangeAxis.setRange(0, 2.5);
            rangeAxis.setTickUnit(new NumberTickUnit(.5));
            //rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            ChartPanel thisChart = new ChartPanel(chart);
            radioButtons.add(button);
            buttonGroup.add(button);

            //button.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            thisChart.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            buttonPanel.add(button);
            chartPanel.add(thisChart);
        }

        radioButtons.get(0).setSelected(true);

        add(buttonPanel);
        add(chartPanel);
    }

    private JFreeChart createSeriesAndChart(String title) {
        XYSeries xySeries = new XYSeries(title);
        LinkedList<Double> data = new LinkedList<Double>() ;
        for (int ii = -25; ii <= 25; ii++ ) {
            xySeries.add(ii, 0);
            data.add((double)0);
        }
        dataSeries.add(data);


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

    public void setDataType(DataType type){
        dataType = type;
    }


    /**
     * Change the plot of the currently selected chart.
     *
     * @param rowNode the user selected row
     * @param colNode the user selected column
     */
    public void changePlot(int rowNode, int colNode){
        JFreeChart chart = plots.get(node);
        chart.setTitle("Node " + rowNode +"-"+ nodes[colNode]);
        if (flashPlot > 0)
        plots.get(flashPlot).setBackgroundPaint(Color.white);
        flashPlot = node;
        flashCount = 15;
        plotNodes[node] = (rowNode-1)*16 + colNode+1;
        if(++node > 4) {
            node = 0;
        }
        radioButtons.get(node).setSelected(true);
    }

    public void changePlot(int index, int rowNode, int colNode){
        node = index;
        if (flashPlot > 0)
        plots.get(flashPlot).setBackgroundPaint(Color.white);
        flashPlot = node;
        flashCount = 15;
        JFreeChart chart = plots.get(node);
        chart.setTitle("Node " + rowNode +"-"+ nodes[colNode]);
        plotNodes[node] = (rowNode-1)*16 + colNode+1;
        radioButtons.get(node).setSelected(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        node = Integer.parseInt(actionEvent.getActionCommand());

    }

    /**
     * Update the plots with the given data.
     *
     * @param data the data to update with
     */
    public void updatePlots(DataLine data){
        if (flashPlot >= 0) {
            if (flashCount > 0 && flashCount-- % 5 == 0)
            {
                plots.get(flashPlot).setBackgroundPaint(Color.white);
            } else {
                plots.get(flashPlot).setBackgroundPaint(Color.green);
            }
            if (flashCount == 0){
                plots.get(flashPlot).setBackgroundPaint(Color.white);
                flashPlot = -1;
            }
        }
        for (int plotNumber = 0; plotNumber < 5; plotNumber++){
            int dataPoint = data.getDataAt(plotNodes[plotNumber]-1);
            double point = translatePoint(dataPoint);
            LinkedList<Double> points = dataSeries.get(plotNumber);
            points.add(point);
            if (points.size() > 50) points.removeFirst();
            ListIterator<Double> listIterator = points.listIterator();
            XYSeries chartSeries = series.get(plotNumber);
            chartSeries.clear();
            for (int ii=-1*points.size()/2; ii < points.size()/2-1; ii++) {
                chartSeries.add(new XYDataItem((double) ii, listIterator.next().doubleValue()), false);
            }
            chartSeries.add(new XYDataItem((double) points.size()/2, listIterator.next().doubleValue()), true);

        }
    }

    public void advanceTime(){
    }

    public void resetPlotsTo(List<DataLine> data, int newTime) {
        for (int plotNumber = 0; plotNumber < 5; plotNumber++){
            series.get(plotNumber).clear();
            dataSeries.get(plotNumber).clear();
        }
        for (int timeNumber = 0; timeNumber < data.size(); timeNumber++) {
            DataLine line = data.get(timeNumber);
            for (int plotNumber = 0; plotNumber < 5; plotNumber++){
                int dataPoint = line.getDataAt(plotNodes[plotNumber]-1);
                double point = translatePoint(dataPoint);
                dataSeries.get(plotNumber).add(point);
                series.get(plotNumber).add(timeNumber-(data.size()/2), point);
            }
        }
    }

    private double translatePoint(int point) {
        switch (dataType) {
        case RAW:
            return (double)(point & 0x00000FFF) / 4096 * 2.5 ;
        case PROCESSED:
            return (double)(point & 0x000003FF) / 1024 * 2.5;
        }
        return 0;
    }
}
