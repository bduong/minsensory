/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

package com.minsensory.gui;

import com.minsensory.data.DataBank;
import com.minsensory.data.DataLine;
import com.minsensory.data.capture.SDCapture;
import com.minsensory.data.playback.DataPopulator;
import com.minsensory.data.playback.FileReader;
import com.minsensory.data.playback.StaticDataBank;
import com.minsensory.data.playback.UpdateTimer;
import com.minsensory.data.realtime.COMReader;
import com.minsensory.data.realtime.DataTimer;
import com.minsensory.freq.FrequencyAnalysisProcessor;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import com.minsensory.gui.MacOS.MacOSEventHandler;
import com.minsensory.gui.menu.items.ValidationTestPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.List;

/**
 * The <code>UI</code> object represents the user interface the user will see
 * when running the application.
 *
 * It is the container for all of the UI objects and their ActionListeners. The
 * <code>UI</code> sets the layout for all container panels like the
 * <code>ColorGrid</code>, <code>PlotPanel</code>, and <code>SelectionPanel</code>.
 *
 * It also does all of the calculations for when the user clicks on the UI
 * either in the plots or the colored image.
 */
public class UI {
    private int windowWidth;
    private int windowHeight;
    private OperatingMode operatingMode;

    private static Integer [] baudRates = new Integer[] { 300, 600, 1200, 1800, 2400, 4800, 7200, 9600, 14400, 19200, 38400, 57600, 115200, 230400, 460800, 921600 };
    private static Integer [] dataBitSettings = new Integer[] { 5, 6, 7, 8 };
    private static String [] paritySettings =  new String[] { "NONE", "ODD", "EVEN", "MARK", "SPACE" };

    public UI() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowWidth = screenSize.width / 2;
        windowHeight = screenSize.height / 2;
        operatingMode = OperatingMode.FROM_FILE;
    }

    public UI(int width, int height, OperatingMode mode) {
        windowWidth = width;
        windowHeight = height;
        operatingMode = mode;
    }

    private DataBank dataBank;

    private JFrame application;

    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem exit;
    private JMenu help;
    private JMenuItem about;

    private JFileChooser fileChooser;
    private File dataFile;

    private PlotPanel plotPanel;
    private ColorMappedImage colorMap;

    private UpdateTimer updater;

    private int buttonLength = 250;
    private int buttonWidth = 100;

    private JButton realTimeSelect;
    private JButton playBackSelect;

    private JButton startRealTimeData;
    private JButton loadData;
    /** buttons for real time data read */

    private JButton startDataRead;
    private JButton stopDataRead;
    private JButton disconnect;
    private JButton connect;

    /** buttons for playback */
    private JButton startPlayBack;
    private JButton stopPlayBack;

    private boolean userSeek = true;
    private JSlider seekSlider;
    private int moveAmount = 100;

    private JButton back;
    private JButton next;

    /** Things for SD Capture */
    private SDCapture sdCapture;

    /** Things for Frequency Analysis */
    private FrequencyAnalysisProcessor frequencyAnalysisProcessor;

    private boolean started;

    private File saveDataFile;

    private GroupLayout layout;

    private String port;
    private Thread dataCollectorThread;
    private DataTimer dataTimer;

    private SelectionPanel selectionPanel;
    private JTextField seekField;
    private JButton seekGoTo;

    private JLabel loadFileLabel;
    private JLabel saveFileLabel;

    private ButtonGroup dataTypeButtonGroup;
    private JRadioButton processedDataRadioButton;
    private JRadioButton rawDataRadioButton;

    private JLabel readingStatus;
    private JButton chooseSaveFile;
    private JComboBox realTimeComBox;
    private JComboBox realTimeBaudBox;
    private JComboBox realTimeDataBox;
    private JComboBox realTimeStopBitsBox;
    private JComboBox realTimeParityBitBox;

    private String comPortName;
    private int baud;
    private int dataBits;
    private int stopBits;
    private int parity;

    /**
     * SD Card Options
     */

    private JLabel sdSaveFileName;
    private JButton sdSaveFileSelect;
    private JComboBox sdComBox;
    private JComboBox sdBaudBox;
    private JComboBox sdDataBox;
    private JComboBox sdStopBitsBox;
    private JComboBox sdParityBitBox;

    private JButton sdStartTransfer;
    private JButton sdStopTransfer;
    private JLabel sdBytesTransferred;

    /**
     * Frequency Analysis Options
     */

    private JLabel freqSaveFileName;
    private JButton freqSaveFileSelect;

    private JFormattedTextField alphaField;
    private JFormattedTextField betaField;
    private JFormattedTextField deltaField;
    private JFormattedTextField gammaField;
    private JFormattedTextField thetaField;

    private JSlider alphaSlider;
    private JSlider betaSlider;
    private JSlider deltaSlider;
    private JSlider gammaSlider;
    private JSlider thetaSlider;

    private List<String> ports;

    private JButton freqProcessButton;
    private ChartPanel freqChartPanel;
    private XYSeries alphaLine;
    private XYSeries betaLine;
    private XYSeries deltaLine;
    private XYSeries gammaLine;
    private XYSeries thetaLine;


    /**
     * Properties
     */

    /** Frequency Analysis */
    private int alphaThreshold;
    private int betaThreshold;
    private int deltaThreshold;
    private int gammaThreshold;
    private int thetaThreshold;




    private COMReader comReader;
    private BufferedOutputStream fileSaveStream;

    private JPopupMenu popupMenu;

    private int rightClickX;
    private int rightClickY;

    boolean firstStart = true;

    /**
     * Enabled or Disabled all of the configuration buttons for real time mode.
     *
     * @param enabled enable
     */
    private void setEnabledForCOMObjects(boolean enabled) {
        startDataRead.setEnabled(enabled);
        stopDataRead.setEnabled(enabled);
        chooseSaveFile.setEnabled(enabled);
        connect.setEnabled(enabled);
        disconnect.setEnabled(enabled);
        realTimeComBox.setEnabled(enabled);
        realTimeBaudBox.setEnabled(enabled);
        realTimeDataBox.setEnabled(enabled);
        realTimeStopBitsBox.setEnabled(enabled);
        realTimeParityBitBox.setEnabled(enabled);
    }

    /**
     * Enabled or Disabled all of the configuration buttons for the play mode.
     *
     * @param enabled enable
     */
    private void setEnabledForPlaybackObjects(boolean enabled) {
        seekField.setEnabled(enabled);
        seekGoTo.setEnabled(enabled);
        back.setEnabled(enabled);
        seekSlider.setEnabled(enabled);
        next.setEnabled(enabled);
        loadData.setEnabled(enabled);
        processedDataRadioButton.setEnabled(enabled);
        rawDataRadioButton.setEnabled(enabled);
        startPlayBack.setEnabled(enabled);
        stopPlayBack.setEnabled(enabled);
    }

    /** Set names for the UI components in order to test. */
    private void setUIComponentNames() {
        application.setName("applicationWindow");
        menuBar.setName("menuBar");
        file.setName("file");
        exit.setName("exit");
        help.setName("help");
        about.setName("about");
    }

    /** Initializes the UI window */
    public void init() {
        changeLookAndFeel();
        started = false;

        application = new JFrame("System For Sensing Neural Response");
        ImageIcon icon = new ImageIcon(
          this.getClass().getResource("/TaskbarIcon.png"));
        application.setIconImage(icon.getImage());
        application.setSize(windowWidth, windowHeight);
        application.setMinimumSize(new Dimension(windowWidth, windowHeight));

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setVisible(false);

        comReader = new COMReader();

        popupMenu = new JPopupMenu();

        for (int ii = 0; ii < 5; ii++) {
            JMenuItem menuItem = new JMenuItem("Show in Plot" + (ii + 1));
            final int index = ii;
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    plotPanel.changePlot(index, rightClickY, rightClickX);
                    colorMap.flashNode();
                }
            });

            popupMenu.add(menuItem);
        }

        layout = new GroupLayout(application.getContentPane());
        application.setLayout(layout);

        fileChooser = new JFileChooser("Choose Data File") {
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane
                      .showConfirmDialog(this, "The file exists, overwrite?",
                        "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                    case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                    case JOptionPane.NO_OPTION:
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        cancelSelection();
                        return;
                    }
                }

                super.approveSelection();
            }
        };
        fileChooser.setMultiSelectionEnabled(false);

        baud = 9600;
        dataBits = SerialPort.DATABITS_8;
        stopBits = SerialPort.STOPBITS_1;


        /**
         * RealTime
         */

        initializeRealTimeButtons();

        /**
         * PlayBack
         */

        initializePlayBackButtons();

        initializeSDCaptureButtons();

        initializeFrequencyButtons();

        seekSlider.setEnabled(false);
        back.setEnabled(false);
        next.setEnabled(false);
        seekGoTo.setEnabled(false);
        seekField.setEnabled(false);
        startPlayBack.setEnabled(false);
        stopPlayBack.setEnabled(false);

        disconnect.setEnabled(false);
        startDataRead.setEnabled(false);
        stopDataRead.setEnabled(false);

        processedDataRadioButton.setSelected(true);

        selectionPanel = new SelectionPanelBuilder().aSelectionPanel()
          /**
           * Play Back
           */
          .withGoToButton(seekGoTo)
          .withSeekEditField(seekField)
          .withBack(back)
          .withNext(next)
          .withSlider(seekSlider)
          .withLoadButton(loadData)
          .withFileLoadNameLabel(loadFileLabel)
          .withProcessedDataRadioButton(processedDataRadioButton)
          .withRawDataRadioButton(rawDataRadioButton)
          .withPlayStartButton(startPlayBack)
          .withPlayStopButton(stopPlayBack)

            /**
             * Real Time
             */
          .withCOMStartButton(startDataRead)
          .withCOMStopButton(stopDataRead)
          .withReadingStatusLabel(readingStatus)
          .withRealTimeSaveButton(chooseSaveFile)
          .withFileSaveNameLabel(saveFileLabel)
          .withRealTimeCOMBox(realTimeComBox)
          .withRealTimeBaudBox(realTimeBaudBox)
          .withRealTimeDataBox(realTimeDataBox)
          .withRealTimeStopBitsBox(realTimeStopBitsBox)
          .withRealTimeParityBox(realTimeParityBitBox)
          .withConnectButton(connect)
          .withDisconnectButton(disconnect)

            /**
             * SD Card Capture
             */
          .withSDFileSaveName(sdSaveFileName)
          .withSDSaveButton(sdSaveFileSelect)
          .withSDCOMBox(sdComBox)
          .withSDBaudBox(sdBaudBox)
          .withSDDataBox(sdDataBox)
          .withSDStopBitsBox(sdStopBitsBox)
          .withSDParityBox(sdParityBitBox)
          .withStartTransferButton(sdStartTransfer)
          .withStopTransferButton(sdStopTransfer)
          .withBytesTransferredLabel(sdBytesTransferred)

            /**
             * Frequency Analysis
             */
          .withFrequencySaveFileLabel(freqSaveFileName)
          .withFrequencySaveButton(freqSaveFileSelect)
          .withAlphaField(alphaField)
          .withBetaField(betaField)
          .withDeltaField(deltaField)
          .withGammaField(gammaField)
          .withThetaField(thetaField)
          .withAlphaSlider(alphaSlider)
          .withBetaSlider(betaSlider)
          .withDeltaSlider(deltaSlider)
          .withGammaSlider(gammaSlider)
          .withThetaSlider(thetaSlider)
          .withProcessButton(freqProcessButton)
          .withFrequencyChart(freqChartPanel)

          .build();

        addMenuBar();

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        plotPanel = new PlotPanel();
        plotPanel
          .setPreferredSize(new Dimension(windowWidth / 4, windowHeight));
        colorMap = new ColorMappedImage(16, 16);
        final double boarderSize = .05;
        colorMap.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int xScale = (int) Math.round(colorMap.getWidth() / 16.0);
                int yScale = (int) Math.round(colorMap.getHeight() / 16.0);
                int x = e.getX() / xScale;
                int y = e.getY() / yScale;
                int xBorder = e.getX() % xScale;
                int yBorder = e.getY() % yScale;

                if ((xBorder > xScale * boarderSize && xBorder < xScale * (1 - boarderSize) && yBorder > yScale * boarderSize && yBorder < yScale * (1 - boarderSize))) {

                    if (e.getButton() == MouseEvent.BUTTON1) {
                        colorMap.clickNode(y, x);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        colorMap.clickNode(y, x);
                        rightClickX = x;
                        rightClickY = y + 1;
                        if (!seekSlider.isEnabled())
                            popupMenu
                              .show(e.getComponent(), e.getX(), e.getY());
                    }

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int xScale = (int) Math.round(colorMap.getWidth() / 16.0);
                int yScale = (int) Math.round(colorMap.getHeight() / 16.0);
                int x = e.getX() / xScale;
                int y = e.getY() / yScale + 1;
                if (y > 16) {
                    y = 16;
                }
                popupMenu.setVisible(false);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int xBorder = e.getX() % xScale;
                    int yBorder = e.getY() % yScale;

                    if ((xBorder > xScale * boarderSize && xBorder < xScale * (1 - boarderSize) && yBorder > yScale * boarderSize && yBorder < yScale * (1 - boarderSize))) {
                        plotPanel.changePlot(y, x);
                        colorMap.flashNode();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    if (colorMap.isANodeHighlighted(y - 1, x)) {
                        colorMap.unclickNode();
                    } else {
                        colorMap.clickNode(y - 1, x);
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    colorMap.unclickNode();
                }
            }

        });
        colorMap.addMouseMotionListener(new MouseMotionAdapter() {
            int col;
            int row;

            @Override
            public void mouseMoved(MouseEvent e) {
                int xScale = (int) Math.round(colorMap.getWidth() / 16.0);
                int yScale = (int) Math.round(colorMap.getHeight() / 16.0);
                int xBorder = e.getX() % xScale;
                int yBorder = e.getY() % yScale;

                if ((xBorder < xScale * boarderSize || xBorder > xScale * (1 - boarderSize) || yBorder < yScale * boarderSize || yBorder > yScale * (1 - boarderSize))) {
                    colorMap.setCursor(Cursor.getDefaultCursor());
                } else {
                    colorMap.setCursor(
                      Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println(e.getModifiers());
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                    int xScale = (int) Math.round(colorMap.getWidth() / 16.0);
                    int yScale = (int) Math.round(colorMap.getHeight() / 16.0);
                    int x = e.getX() / xScale;
                    int y = e.getY() / yScale;
                    int xBorder = e.getX() % xScale;
                    int yBorder = e.getY() % yScale;

                    if ((xBorder > xScale * boarderSize && xBorder < xScale * (1 - boarderSize) && yBorder > yScale * boarderSize && yBorder < yScale * (1 - boarderSize))) {
                        if (x != col || y != row) {
                            colorMap.clickNode(y, x);
                            col = x;
                            row = y;
                        }
                    } else {
                        colorMap.unclickNode();
                    }
                }
            }

        }
        );

        //Color Plot
        int[] colors = new int[256];
        for (int i = 0; i < 256; i++) {
            colors[i] = Integer.MAX_VALUE * ((i / 16 % 2 + i) % 2);
        }
        colorMap.setColors(colors);

        final ColorGrid colorGrid = new ColorGrid(colorMap);
        colorGrid.setPreferredSize(new Dimension(windowWidth / 3,
          windowWidth / 3));

        Dimension selectionDim = colorGrid.getMaximumSize();
        selectionPanel.setMaximumSize(
          new Dimension(selectionDim.width, (int) (windowHeight * .3)));
        selectionPanel.setBorder(BorderFactory.createEtchedBorder());

        colorMap.setDataType(DataType.PROCESSED);
        plotPanel.setDataType(DataType.PROCESSED);
        /**
         * Set Layouts
         */
        layout.setHonorsVisibility(false);

        layout.setHorizontalGroup(layout.createSequentialGroup()
          .addGap(0, 0, 10)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(colorGrid)
            .addComponent(selectionPanel)
          )
          .addGap(0, 0, 100)
          .addComponent(plotPanel)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(
              layout.createSequentialGroup()
                .addComponent(colorGrid, 0, GroupLayout.PREFERRED_SIZE,
                  Short.MAX_VALUE)
                .addGap(0, 10, 20)
                .addComponent(selectionPanel)
            )
            .addComponent(plotPanel, 0, GroupLayout.PREFERRED_SIZE,
              Short.MAX_VALUE)));

        setUIComponentNames();
    }

    /** Change the Look and Feel of the UI */
    private void changeLookAndFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }

    /** Initialize the buttons for real time mode */
    private void initializeRealTimeButtons() {
        saveFileLabel = new JLabel("Saving To: ");
        saveFileLabel.setOpaque(true);

        readingStatus = new JLabel("Reading Status: Disconnected");
        readingStatus.setOpaque(true);
        readingStatus.setForeground(Color.BLACK);
        readingStatus.setBackground(Color.RED);
        chooseSaveFile = new JButton("Select Save File");
        chooseSaveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showSaveDialog(application);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    saveDataFile = fileChooser.getSelectedFile();
                    try {
                        if (fileSaveStream != null) fileSaveStream.close();
                        fileSaveStream = new BufferedOutputStream(
                          new FileOutputStream(saveDataFile));
                        saveFileLabel
                          .setText("Saving To: " + saveDataFile.getName());
                        saveFileLabel.setBackground(Color.GREEN);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (!connect.isEnabled()) {
                        startDataRead.setEnabled(true);
                    }
                }
            }
        });

        try {
            ports = COMReader.listPorts();
            String[] portNames = new String[ports.size()];
            ports.toArray(portNames);
            for (String name: portNames) {
                System.out.println(name);
            }
            if (ports.size() > 0) comPortName = portNames[0];
            else comPortName = "";
            if (portNames.length <= 0)
                realTimeComBox = new JComboBox(new String[] { "None" });
            else realTimeComBox = new JComboBox(portNames);
        } catch (NoSuchPortException e) {
            realTimeComBox = new JComboBox(new String[] { "None" });
        }

        realTimeComBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                int index = realTimeComBox.getSelectedIndex();
                comPortName = ports.get(index);
            }
        });

        realTimeBaudBox = new JComboBox(baudRates);
        realTimeBaudBox.setEditable(true);
        realTimeBaudBox.setSelectedItem(9600);
        realTimeBaudBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baud = (Integer) realTimeBaudBox.getSelectedItem();
            }
        });
        realTimeDataBox = new JComboBox(dataBitSettings);
        realTimeDataBox.setSelectedItem(8);
        realTimeDataBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bits = (Integer) realTimeDataBox.getSelectedItem();
                switch (bits) {
                case 5:
                    dataBits = SerialPort.DATABITS_5;
                case 6:
                    dataBits = SerialPort.DATABITS_6;
                case 7:
                    dataBits = SerialPort.DATABITS_7;
                case 8:
                    dataBits = SerialPort.DATABITS_8;
                }
            }
        });
        realTimeStopBitsBox = new JComboBox(new String[] { "1", "2", "1.5" });
        realTimeStopBitsBox.setSelectedItem("1");
        realTimeStopBitsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = realTimeStopBitsBox.getSelectedIndex();
                switch (index) {
                case 0:
                    stopBits = SerialPort.STOPBITS_1;
                case 1:
                    stopBits = SerialPort.STOPBITS_2;
                case 2:
                    stopBits = SerialPort.STOPBITS_1_5;
                }
            }
        });
        realTimeParityBitBox = new JComboBox(
          new String[] { "NONE", "ODD", "EVEN", "MARK", "SPACE" });
        realTimeParityBitBox.setSelectedItem("NONE");
        realTimeParityBitBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = realTimeStopBitsBox.getSelectedIndex();
                switch (index) {
                case 0:
                    parity = SerialPort.PARITY_NONE;
                case 1:
                    parity = SerialPort.PARITY_ODD;
                case 2:
                    parity = SerialPort.PARITY_EVEN;
                case 3:
                    parity = SerialPort.PARITY_MARK;
                case 4:
                    parity = SerialPort.PARITY_SPACE;
                }
            }
        });
        parity = SerialPort.PARITY_NONE;

        startDataRead = new JButton("Start");
        startDataRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (firstStart) {
                    try {
                        startDataCollection();
                        dataTimer.setSaveFileOutputStream(fileSaveStream);
                        firstStart = false;
                        dataTimer.startTimer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    dataTimer.resumeUI();
                }
                startDataRead.setEnabled(false);
                stopDataRead.setEnabled(true);
                disconnect.setEnabled(false);
                connect.setEnabled(false);
                readingStatus
                  .setText("Reading Status: Reading from " + comPortName);
                readingStatus.setBackground(Color.GREEN);

            }
        });

        stopDataRead = new JButton("Pause");
        stopDataRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dataTimer.pauseUI();
                startDataRead.setEnabled(true);
                stopDataRead.setEnabled(false);
                disconnect.setEnabled(true);
                chooseSaveFile.setEnabled(false);
            }
        });

        disconnect = new JButton("Disconnect");
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dataTimer.stopTimer();
                comReader.closeStreams();
                setEnabledForCOMObjects(true);
                setEnabledForPlaybackObjects(true);
                startDataRead.setEnabled(false);
                stopDataRead.setEnabled(false);
                disconnect.setEnabled(false);
                connect.setEnabled(true);
                chooseSaveFile.setEnabled(true);
                readingStatus.setText("Reading Status: Disconnected");
                readingStatus.setBackground(Color.RED);
            }
        });

        connect = new JButton("Connect");
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                operatingMode = OperatingMode.FROM_COM_PORT;

                plotPanel.setDataType(DataType.PROCESSED);
                colorMap.setDataType(DataType.PROCESSED);

                setEnabledForPlaybackObjects(false);
                setEnabledForCOMObjects(false);
                disconnect.setEnabled(true);
                chooseSaveFile.setEnabled(true);
                if (saveDataFile != null) startDataRead.setEnabled(true);
                try {
                    comReader
                      .connectTo(comPortName, baud, dataBits, stopBits, parity);
//                    comReader.connectTo(comPortName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /** Initialize the buttons for play back mode */
    private void initializePlayBackButtons() {

        seekGoTo = new JButton("Go To:");
        seekGoTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.valueOf(seekField.getText());
                    if (value < seekSlider.getMaximum() && value > 0) {
                        userSeek = false;
                        seekSlider.setValue(value);
                        seekTime();
                    }
                } catch (Exception ex) {
                    seekField.setText("");
                }
            }
        });
        seekField = new JTextField(10);
        loadFileLabel = new JLabel("Loaded: ");

        processedDataRadioButton = new JRadioButton("Processed Data");
        processedDataRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorMap.setDataType(DataType.PROCESSED);
                plotPanel.setDataType(DataType.PROCESSED);
            }
        });
        rawDataRadioButton = new JRadioButton("Raw Data");
        rawDataRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorMap.setDataType(DataType.RAW);
                plotPanel.setDataType(DataType.RAW);
            }
        });

        dataTypeButtonGroup = new ButtonGroup();
        dataTypeButtonGroup.add(processedDataRadioButton);
        dataTypeButtonGroup.add(rawDataRadioButton);

        playBackSelect = new JButton("Play Back Data");
        playBackSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                operatingMode = OperatingMode.FROM_FILE;
                realTimeSelect.setEnabled(true);
                playBackSelect.setEnabled(false);
                startRealTimeData.setVisible(false);
                loadData.setVisible(true);
            }
        });

        loadData = new JButton("Load File");
        loadData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int returnVal = fileChooser.showOpenDialog(application);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    operatingMode = OperatingMode.FROM_FILE;
                    dataFile = fileChooser.getSelectedFile();

                    if (dataBank != null) dataBank.clear();
                    else dataBank = new StaticDataBank();
                    loadFileLabel.setText("Loaded: " + dataFile.getName());
                    loadFileLabel.setOpaque(true);
                    try {
                        DataPopulator populator = new DataPopulator(dataBank,
                          new FileReader(dataFile));
                        populator.execute();
                        int dataBankSize = dataBank.getSize();
                        seekSlider.setMaximum(dataBankSize);
                        seekSlider.setMajorTickSpacing(dataBankSize / 20);
                        seekSlider.setMinorTickSpacing(dataBankSize / 200);
                        int tickSize = (int) Math
                          .ceil(Math.log10(dataBankSize / 20));
                        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
                        for (int ii = 0; ii <= 20; ii++) {
                            labelTable.put(dataBankSize / 20 * ii, new JLabel(
                              String.format("%.1f",
                                ((float) dataBankSize) / 20 / (Math
                                  .pow(10, tickSize)) * ii)));
                        }   //@TODO put some sort of modifer here to length of data file
                        seekSlider.setLabelTable(labelTable);
                        loadFileLabel.setBackground(Color.GREEN);
                        setEnabledForCOMObjects(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        loadFileLabel.setBackground(Color.RED);
                    }

                    seekField.setEnabled(true);
                    seekGoTo.setEnabled(true);
                    next.setEnabled(true);
                    seekSlider.setEnabled(true);
                    back.setEnabled(true);
                    startPlayBack.setEnabled(true);
                    stopPlayBack.setEnabled(false);

                }
            }
        }
        );

        startPlayBack = new JButton("Start");
        startPlayBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!started) {
                    updater = new UpdateTimer(1000 / 30, colorMap, plotPanel,
                      dataBank);
                    updater.setSlider(seekSlider);
                    updater.setApplication(UI.this);
                    started = true;
                }
                seekSlider.setEnabled(false);
                back.setEnabled(false);
                next.setEnabled(false);
                seekField.setEnabled(false);
                seekGoTo.setEnabled(false);
                updater.startTimer();
                startPlayBack.setEnabled(false);
                stopPlayBack.setEnabled(true);
                processedDataRadioButton.setEnabled(false);
                rawDataRadioButton.setEnabled(false);
            }
        });
        stopPlayBack = new JButton("Stop");
        stopPlayBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updater.stopTimer();
                startPlayBack.setEnabled(true);
                stopPlayBack.setEnabled(false);
                seekSlider.setEnabled(true);
                back.setEnabled(true);
                next.setEnabled(true);
                seekField.setEnabled(true);
                seekGoTo.setEnabled(true);
                processedDataRadioButton.setEnabled(true);
                rawDataRadioButton.setEnabled(true);
            }
        });

        seekSlider = new JSlider(JSlider.HORIZONTAL, 0, 1, 0);
        seekSlider.setPaintLabels(true);
        seekSlider.setPaintTicks(true);
        seekSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (userSeek) {
                    int value = seekSlider.getValue();
                    if (value >= seekSlider.getMaximum()) {
                        seekSlider.setValue(value - 1);
                    }
                    seekTime();
                } else {
                    userSeek = true;
                }
            }
        });

        next = new JButton(">>");

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int value = seekSlider.getValue();
                if (value < seekSlider.getMaximum() - 1 - moveAmount) {
                    seekSlider.setValue(value + moveAmount);
                } else {
                    seekSlider.setValue(seekSlider.getMaximum() - 1);
                }
                userSeek = false;
                seekTime();
            }
        });
        back = new JButton("<<");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int value = seekSlider.getValue();
                if (value >= moveAmount) {
                    seekSlider.setValue(value - moveAmount);
                } else {
                    seekSlider.setValue(0);
                }
                userSeek = false;
                seekTime();
            }
        });
    }

    private void initializeSDCaptureButtons() {
        sdCapture = new SDCapture();
        try {
            List<String> ports = COMReader.listPorts();
            String[] portNames = new String[ports.size()];
            ports.toArray(portNames);
            if (ports.size() > 0) comPortName = portNames[0];
            else comPortName = "";
            if (portNames.length <= 0)
                sdComBox = new JComboBox(new String[] { "None" });
            else sdComBox = new JComboBox(portNames);
        } catch (NoSuchPortException e) {
            sdComBox = new JComboBox(new String[] { "None" });
        }


        sdBaudBox = new JComboBox(baudRates);
        sdBaudBox.setEditable(true);
        sdBaudBox.setSelectedItem(baud);
        sdBaudBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sdCapture.setBaud((Integer) sdBaudBox.getSelectedItem());
            }
        });

        sdDataBox = new JComboBox(dataBitSettings);
        sdDataBox.setSelectedItem(8);
        sdDataBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bits = (Integer) sdDataBox.getSelectedItem();
                switch (bits) {
                case 5:
                    sdCapture.setDataBits(SerialPort.DATABITS_5);
                case 6:
                    sdCapture.setDataBits(SerialPort.DATABITS_6);
                case 7:
                    sdCapture.setDataBits(SerialPort.DATABITS_7);
                case 8:
                    sdCapture.setDataBits(SerialPort.DATABITS_8);
                }
            }
        });

        sdStopBitsBox = new JComboBox(new String[] { "1", "2", "1.5" });
        sdStopBitsBox.setSelectedItem("1");
        sdStopBitsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = sdStopBitsBox.getSelectedIndex();
                switch (index) {
                case 0:
                    sdCapture.setStopBits(SerialPort.STOPBITS_1);
                case 1:
                    sdCapture.setStopBits(SerialPort.STOPBITS_2);
                case 2:
                    sdCapture.setStopBits(SerialPort.STOPBITS_1_5);
                }
            }
        });

        sdParityBitBox = new JComboBox(paritySettings);
        sdParityBitBox.setSelectedItem("NONE");
        sdParityBitBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = sdParityBitBox.getSelectedIndex();
                switch (index) {
                case 0:
                    sdCapture.setParity(SerialPort.PARITY_NONE);
                case 1:
                    sdCapture.setParity(SerialPort.PARITY_ODD);
                case 2:
                    sdCapture.setParity(SerialPort.PARITY_EVEN);
                case 3:
                    sdCapture.setParity(SerialPort.PARITY_MARK);
                case 4:
                    sdCapture.setParity(SerialPort.PARITY_SPACE);
                }
            }
        });


        sdSaveFileName = new JLabel("NONE");
        sdSaveFileSelect = new JButton("Select");
        sdSaveFileSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int returnVal = fileChooser.showSaveDialog(application);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    saveDataFile = fileChooser.getSelectedFile();
                    sdSaveFileName.setText(saveDataFile.getName());
                    sdSaveFileName.setBackground(Color.GREEN);
                    sdCapture.setSaveFile(saveDataFile);
                }
            }
        });
        sdStartTransfer = new JButton("Start Transfer");
        sdStartTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    sdCapture.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sdStopTransfer = new JButton("Stop Transfer");
        sdStartTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    sdCapture.stop();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
        sdBytesTransferred = new JLabel("0 B");
        sdCapture.setBytesLabel(sdBytesTransferred);
    }

    private void initializeFrequencyButtons() {
        frequencyAnalysisProcessor = new FrequencyAnalysisProcessor();
        freqSaveFileName = new JLabel("");
        freqSaveFileSelect = new JButton("Select");
        freqSaveFileSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int returnVal = fileChooser.showSaveDialog(application);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    saveDataFile = fileChooser.getSelectedFile();
                    freqSaveFileName.setText(saveDataFile.getName());
                    freqSaveFileName.setBackground(Color.GREEN);
                    try {
                        frequencyAnalysisProcessor.setLoadFile(saveDataFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        alphaField = createThresholdField();
        betaField = createThresholdField();
        deltaField = createThresholdField();
        gammaField = createThresholdField();
        thetaField = createThresholdField();

        alphaSlider = createThresholdSlider();
        betaSlider = createThresholdSlider();
        deltaSlider = createThresholdSlider();
        gammaSlider = createThresholdSlider();
        thetaSlider = createThresholdSlider();

        freqProcessButton = new JButton("Process Data");
        freqProcessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createNewSaveFile(saveDataFile);
                frequencyAnalysisProcessor.init();
                try {
                    frequencyAnalysisProcessor.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void createNewSaveFile(File loadFile) {
                try {
                    frequencyAnalysisProcessor.setSaveFile(new File(loadFile.getName() + "_processed.bin"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        alphaLine = createFreqLine("Alpha");
        betaLine = createFreqLine("Beta");
        deltaLine = createFreqLine("Delta");
        gammaLine = createFreqLine("Gamma");
        thetaLine = createFreqLine("Theta");

        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        seriesCollection.addSeries(alphaLine);
        seriesCollection.addSeries(betaLine);
        seriesCollection.addSeries(deltaLine);
        seriesCollection.addSeries(gammaLine);
        seriesCollection.addSeries(thetaLine);


        freqChartPanel = new ChartPanel(ChartFactory.createXYLineChart(
          null,
          null,
          null,
          seriesCollection,
          PlotOrientation.VERTICAL,
          false,
          true,
          false
        ));

        freqChartPanel.getChart().getXYPlot().getRenderer().setSeriesPaint(0, new Color(255,0,0));
        freqChartPanel.getChart().getXYPlot().getRenderer().setSeriesPaint(1, new Color(0,0, 255));
        freqChartPanel.getChart().getXYPlot().getRenderer().setSeriesPaint(2, new Color(255,0,127));
        freqChartPanel.getChart().getXYPlot().getRenderer().setSeriesPaint(3, new Color(0,255,0));
        freqChartPanel.getChart().getXYPlot().getRenderer().setSeriesPaint(4, new Color(127,255,0));

        NumberAxis rangeAxis = (NumberAxis) freqChartPanel.getChart().getXYPlot().getRangeAxis();
        rangeAxis.setRange(0, 100);
        rangeAxis.setTickUnit(new NumberTickUnit(25));
        NumberAxis domainAxis = (NumberAxis) freqChartPanel.getChart().getXYPlot().getDomainAxis();
        domainAxis.setRange(-10,10);
        domainAxis.setTickUnit(new NumberTickUnit(1));

        for (int ii = 0; ii < 5; ii++){
            freqChartPanel.getChart().getXYPlot().getRenderer().setSeriesStroke(ii, new BasicStroke(5.0f));
        }
    }

    private XYSeries createFreqLine(String name) {
        XYSeries line = new XYSeries(name);
        line.add(-10, 50);
        line.add(10,50);
        return line;
    }

    private JFormattedTextField createThresholdField() {
        JFormattedTextField field = new JFormattedTextField();
        field.setValue(50);
        field.setColumns(3);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.addPropertyChangeListener("value", new FreqThresholdFieldChangeListener());
        return field;
    }

    private JSlider createThresholdSlider() {
        JSlider slider = new JSlider(0,100,50);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(10);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.addChangeListener(new FreqSliderChangeListener());
        return slider;
    }

    private class FreqThresholdFieldChangeListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Object source = propertyChangeEvent.getSource();
            if(source == alphaField) {
                alphaThreshold = (Integer) alphaField.getValue();
                alphaSlider.setValue(alphaThreshold);
                changeThresholdLine(alphaLine, alphaThreshold);
            } else if(source == betaField) {
                betaThreshold = (Integer)  betaField.getValue();
                betaSlider.setValue(betaThreshold);
                changeThresholdLine(betaLine, betaThreshold);
            } else if(source == deltaField) {
                deltaThreshold = (Integer) deltaField.getValue();
                deltaSlider.setValue(deltaThreshold);
                changeThresholdLine(deltaLine, deltaThreshold);
            } else if(source == gammaField) {
                gammaThreshold = (Integer) gammaField.getValue();
                gammaSlider.setValue(gammaThreshold);
                changeThresholdLine(gammaLine, gammaThreshold);
            } else {
                thetaThreshold = (Integer) thetaField.getValue();
                thetaSlider.setValue(thetaThreshold);
                changeThresholdLine(thetaLine, thetaThreshold);
            }
        }


    }
    private void changeThresholdLine(XYSeries line, int threshold) {
        line.clear();
        line.add(-10, threshold);
        line.add(10, threshold);
    }

    private class FreqSliderChangeListener implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            Object source = changeEvent.getSource();
            if(source == alphaSlider) {
                alphaThreshold = alphaSlider.getValue();
                alphaField.setValue(alphaThreshold);
                frequencyAnalysisProcessor.setAlphaThreshold(alphaThreshold);
            } else if(source == betaSlider) {
                betaThreshold = betaSlider.getValue();
                betaField.setValue(betaThreshold);
                frequencyAnalysisProcessor.setBetaThreshold(betaThreshold);
            } else if(source == deltaSlider) {
                deltaThreshold = deltaSlider.getValue();
                deltaField.setValue(deltaThreshold);
                frequencyAnalysisProcessor.setDeltaThreshold(deltaThreshold);
            } else if(source == gammaSlider) {
                gammaThreshold = gammaSlider.getValue();
                gammaField.setValue(gammaThreshold);
                frequencyAnalysisProcessor.setGammaThreshold(gammaThreshold);
            } else {
                thetaThreshold = thetaSlider.getValue();
                thetaField.setValue(thetaThreshold);
                frequencyAnalysisProcessor.setThetaThreshold(thetaThreshold);
            }
        }
    }

    /** Resets the plots and image to a certain time for playback mode */
    private void seekTime() {
        int value;
        value = seekSlider.getValue();
        int begin = value - 50;
        if (begin < 0) begin = 0;
        dataBank.resetTo(value);
        List<DataLine> dataLineList = dataBank.getPoints(begin, value);
        plotPanel.resetPlotsTo(dataLineList, value);
        colorMap.updateImage(dataBank.getPoint(value));
    }

    private JMenu tools;
    private JMenu setup;
    private JMenuItem validate;

    /** Add the Menu Bar to the Application. */
    private void addMenuBar() {
        if (onMac()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        menuBar = new JMenuBar();
        file = new JMenu("File");
        file.setMnemonic('F');

        tools = new JMenu("Tools");
        tools.setMnemonic('T');

        setup = new JMenu("Setup");
        setup.setMnemonic('S');

        validate = new JMenuItem("System Validation Test");
        validate.setMnemonic('V');
        validate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("System Validation Test");
                final ValidationTestPanel validationTest = new ValidationTestPanel();
                frame.add(validationTest, BorderLayout.CENTER);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        validationTest.shutdownClean();
                    }
                });

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

        exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        help = new JMenu("Help");
        help.setMnemonic('H');
        about = new JMenuItem("About");
        about.setMnemonic('A');
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutDialog();
            }
        });

        if (onMac()) {
            new MacOSEventHandler();
        } else {
            file.add(exit);
            help.add(about);
        }

        tools.add(setup);
        setup.add(validate);

        menuBar.add(file);
        menuBar.add(tools);
        menuBar.add(help);

        application.setJMenuBar(menuBar);
    }

    /**
     * Start the collection of Data
     *
     * @throws URISyntaxException if the file cannot be found
     * @throws IOException if the file cannot be read
     */
    private void startDataCollection() throws Exception, IOException {
        switch (operatingMode) {
        case FROM_FILE:
            dataBank = new StaticDataBank();
            DataPopulator populator = new DataPopulator(dataBank,
              new FileReader(dataFile));
            populator.execute();
            break;
        case FROM_COM_PORT:
            dataTimer = new DataTimer(comReader, fileSaveStream);
            dataTimer.setPlotPanel(plotPanel);
            dataTimer.setImage(colorMap);
            break;
        }
    }

    /** Maximizes the windows and set the UI to be visible. */
    public void run() {
        if (application != null) {
            application.setExtendedState(JFrame.MAXIMIZED_BOTH);

            application.setVisible(true);
        }
    }

    /**
     * Defines whether the application is running on a Mac or not
     *
     * @return true if on a Mac, false otherwise;
     */
    private boolean onMac() {
        return (System.getProperty("mrj.version") != null);
    }

    /** Reports that the data bank is out of data thus reenable some buttons */
    public void reportFinish() {
        startPlayBack.setEnabled(true);
        stopPlayBack.setEnabled(false);
        seekSlider.setEnabled(true);
        back.setEnabled(true);
        next.setEnabled(true);
    }

    /**
     * Set the user seek property.
     *
     * @param seek user seek
     */
    public void setUserSeek(boolean seek) {
        userSeek = seek;
    }
}
