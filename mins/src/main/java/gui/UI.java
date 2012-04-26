package gui;

import data.DataBank;
import data.DataLine;
import data.playback.DataPopulator;
import data.playback.FileReader;
import data.playback.StaticDataBank;
import data.realtime.COMReader;
import data.realtime.DataCollector;
import data.realtime.DataTimer;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import gui.MacOS.MacOSEventHandler;
import gui.menu.items.ValidationTestPanel;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.List;

/**
 * User Interface
 */
public class UI {
    private int windowWidth;
    private int windowHeight;
    private OperatingMode operatingMode;

    public UI(OperatingMode mode) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowWidth = screenSize.width/2;
        windowHeight = screenSize.height/2;
        operatingMode = mode;
    }

    public UI(int width, int height, OperatingMode mode) {
        windowWidth = width;
        windowHeight = height;
        operatingMode = mode;
    }

    private DataBank dataBank;
    private DataCollector dataCollector;

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
    /**
     * buttons for real time data read
     */

    private JButton startDataRead;
    private JButton stopDataRead;
    private JButton disconnect;
    private JButton connect;

    /**
     * buttons for playback
     */
    private JButton startPlayBack;
    private JButton stopPlayBack;

    private boolean userSeek = true;
    private JSlider seekSlider;
    private int moveAmount = 100;

    private JButton back;
    private JButton next;

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
    private JComboBox comBox;
    private JComboBox baudBox;
    private JComboBox dataBox;
    private JComboBox stopBitsBox;
    private JComboBox parityBitBox;

    private String comPortName;
    private int baud;
    private int dataBits;
    private int stopBits;
    private int parity;

    private COMReader comReader;
    private BufferedOutputStream fileSaveStream;

    private JPopupMenu popupMenu;

    private int rightClickX;
    private int rightClickY;


    boolean firstStart = true;

    private void setEnabledForCOMObjects(boolean enabled) {
        startDataRead.setEnabled(enabled);
        stopDataRead.setEnabled(enabled);
        chooseSaveFile.setEnabled(enabled);
        connect.setEnabled(enabled);
        disconnect.setEnabled(enabled);
        comBox.setEnabled(enabled);
        baudBox.setEnabled(enabled);
        dataBox.setEnabled(enabled);
        stopBitsBox.setEnabled(enabled);
        parityBitBox.setEnabled(enabled);
    }

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

    /**
     * Initializes the UI window
     */
    public void init() {
        changeLookAndFeel();
        started = false;

        application = new JFrame("System For Sensing Neural Response");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/TaskbarIcon.png"));
        application.setIconImage(icon.getImage());
        application.setSize(windowWidth, windowHeight);
        application.setMinimumSize(new Dimension(windowWidth, windowHeight));

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setVisible(false);

        comReader = new COMReader();

        popupMenu = new JPopupMenu();


        for (int ii = 0; ii < 5; ii++) {
            JMenuItem menuItem = new JMenuItem("Show in Plot" + (ii+1));
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

            Object[] options = {"Raw Data", "Signal Processed Data"};
            @Override
            public void approveSelection(){
                File f = getSelectedFile();
                if(f.exists() && getDialogType() == SAVE_DIALOG){
                    int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
                    switch(result){
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

        /**
         * RealTime
         */

        initializeRealTimeButtons();

        /**
         * PlayBack
         */

        initializePlayBackButtons();

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
                .withGoToButton(seekGoTo)
                .withSeekEditField(seekField)
                .withBack(back)
                .withNext(next)
                .withSlider(seekSlider)
                .withLoadButton(loadData)
                .withFileLoadNameLabel(loadFileLabel)
                .withButtonGroup(dataTypeButtonGroup)
                .withProcessedDataRadioButton(processedDataRadioButton)
                .withRawDataRadioButton(rawDataRadioButton)
                .withPlayStartButton(startPlayBack)
                .withPlayStopButton(stopPlayBack)

                .withCOMStartButton(startDataRead)
                .withCOMStopButton(stopDataRead)
                .withReadingStatusLabel(readingStatus)
                .withSaveButton(chooseSaveFile)
                .withFileSaveNameLabel(saveFileLabel)
                .withConnectButton(connect)
                .withDisconnectButton(disconnect)
                .withCOMBox(comBox)
                .withBaudBox(baudBox)
                .withDataBox(dataBox)
                .withStopBitsBox(stopBitsBox)
                .withParityBox(parityBitBox)
                .build();





        addMenuBar();

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        plotPanel = new PlotPanel();
        plotPanel.setPreferredSize(new Dimension(windowWidth / 4, windowHeight));
        colorMap = new ColorMappedImage(16,16);
        final double boarderSize = .05;
        colorMap.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {                
                int xScale = (int)Math.round(colorMap.getWidth()/16.0);
                int yScale = (int)Math.round(colorMap.getHeight()/16.0);
                int x = e.getX()/xScale;
                int y = e.getY()/yScale;
                int xBorder = e.getX() % xScale;
                int yBorder = e.getY() % yScale;

                if ((xBorder > xScale * boarderSize && xBorder < xScale *(1-boarderSize) && yBorder > yScale * boarderSize && yBorder < yScale *(1-boarderSize))) {

                    if (e.getButton() == MouseEvent.BUTTON1) {
                        colorMap.clickNode(y, x);
                    }
                    else if (e.getButton() == MouseEvent.BUTTON3) {
                        colorMap.clickNode(y, x);
                        rightClickX = x;
                        rightClickY = y+1;
                        if(!seekSlider.isEnabled())
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }


                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int xScale = (int)Math.round(colorMap.getWidth()/16.0);
                int yScale = (int)Math.round(colorMap.getHeight()/16.0);
                int x = e.getX()/xScale;
                int y = e.getY()/yScale + 1;
                if (y > 16) {
                    y=16;
                }
                popupMenu.setVisible(false);
                if(e.getButton() == MouseEvent.BUTTON1) {
                    int xBorder = e.getX() % xScale;
                    int yBorder = e.getY() % yScale;

                    if ((xBorder > xScale * boarderSize && xBorder < xScale *(1-boarderSize) && yBorder > yScale * boarderSize && yBorder < yScale *(1-boarderSize))) {
                        plotPanel.changePlot(y,x);
                        colorMap.flashNode();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    if(colorMap.isANodeHighlighted(y-1, x)){
                        colorMap.unclickNode();
                    } else {
                        colorMap.clickNode(y-1,x);
                    }
                } else if(e.getButton() == MouseEvent.BUTTON3) {
                    colorMap.unclickNode();
                }
            }

        });
        colorMap.addMouseMotionListener(new MouseMotionAdapter() {
            int col;
            int row;

            @Override
            public void mouseMoved(MouseEvent e) {
                int xScale = (int)Math.round(colorMap.getWidth()/16.0);
                int yScale = (int)Math.round(colorMap.getHeight()/16.0);
                int xBorder = e.getX() % xScale;
                int yBorder = e.getY() % yScale;

                if ((xBorder < xScale * boarderSize || xBorder > xScale *(1-boarderSize) || yBorder < yScale * boarderSize || yBorder > yScale *(1-boarderSize))) {
                    colorMap.setCursor(Cursor.getDefaultCursor());
                } else {
                    colorMap.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println(e.getModifiers());
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                    int xScale = (int)Math.round(colorMap.getWidth()/16.0);
                    int yScale = (int)Math.round(colorMap.getHeight()/16.0);
                    int x = e.getX()/xScale;
                    int y = e.getY()/yScale;
                    int xBorder = e.getX() % xScale;
                    int yBorder = e.getY() % yScale;


                    if ((xBorder > xScale * boarderSize && xBorder < xScale *(1-boarderSize) && yBorder > yScale * boarderSize && yBorder < yScale *(1-boarderSize))) {
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
        int [] colors = new int[256];
        for (int i = 0; i <256; i++) {
            colors[i] = Integer.MAX_VALUE * ((i/16 % 2 + i) % 2);
        }
        colorMap.setColors(colors);


        final ColorGrid colorGrid = new ColorGrid(colorMap);
        colorGrid.setPreferredSize(new Dimension(windowWidth / 3,
                windowWidth / 3));

        Dimension selectionDim = colorGrid.getMaximumSize();
        selectionPanel.setMaximumSize(new Dimension(selectionDim.width, (int) (windowHeight * .3)));
        selectionPanel.setBorder(BorderFactory.createEtchedBorder());

        colorMap.setDataType(DataType.PROCESSED);
        plotPanel.setDataType(DataType.PROCESSED);
        /**
         * Set Layouts
         */
        layout.setHonorsVisibility(false);
//        layout.setHonorsVisibility(seekSlider, true);
//        layout.setHonorsVisibility(back, true);
//        layout.setHonorsVisibility(next, true);

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
                                        .addGap(0, 0, 20)
                        )
                        .addComponent(plotPanel, 0, GroupLayout.PREFERRED_SIZE,
                                Short.MAX_VALUE)));

//        layout.linkSize(SwingConstants.HORIZONTAL, realTimeSelect, playBackSelect);
//        layout.linkSize(SwingConstants.HORIZONTAL, realTimeSelect,
//                startRealTimeData);
//        layout.linkSize(SwingConstants.HORIZONTAL, realTimeSelect, loadData);
//        layout.linkSize(SwingConstants.VERTICAL, realTimeSelect, playBackSelect);
//        layout.linkSize(SwingConstants.VERTICAL, realTimeSelect,
//                startRealTimeData);
//        layout.linkSize(SwingConstants.VERTICAL, realTimeSelect, loadData);
//        layout.linkSize(SwingConstants.VERTICAL, next, back);
//        layout.linkSize(SwingConstants.HORIZONTAL, next, back);

        setUIComponentNames();
    }

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

    private void initializeRealTimeButtons(){
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

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    saveDataFile = fileChooser.getSelectedFile();
                    try {
                        if (fileSaveStream != null) fileSaveStream.close();
                        fileSaveStream = new BufferedOutputStream(new FileOutputStream(saveDataFile));
                        saveFileLabel.setText("Saving To: " + saveDataFile.getName());
                        saveFileLabel.setBackground(Color.GREEN);
                    } catch(Exception ex) {ex.printStackTrace();}

                    if(!connect.isEnabled()) {
                        startDataRead.setEnabled(true);
                    }
                }
            }
        });

        try {
            List<String> ports = COMReader.listPorts();
            String [] portNames = new String[ports.size()];
            ports.toArray(portNames);
            if (ports.size() > 0) comPortName = ports.get(0);
            else comPortName="";
            if(portNames.length <= 0) comBox = new JComboBox(new String[] {"None"});
            else comBox = new JComboBox(portNames);
        } catch (NoSuchPortException e) {
            comBox = new JComboBox(new String[] {"None"});
        };
        baudBox = new JComboBox(new Integer[] {300, 600, 1200, 1800, 2400, 4800, 7200, 9600, 14400, 19200, 38400, 57600, 115200, 230400, 460800, 921600});
        baudBox.setEditable(true);
        baudBox.setSelectedItem(9600);
        baudBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baud = (Integer)baudBox.getSelectedItem();
            }
        });
        baud = 9600;
        dataBox = new JComboBox(new Integer[] {5, 6, 7, 8});
        dataBox.setSelectedItem(8);
        dataBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bits = (Integer) dataBox.getSelectedItem();
                switch (bits) {
                    case 5: dataBits = SerialPort.DATABITS_5;
                    case 6: dataBits = SerialPort.DATABITS_6;
                    case 7: dataBits = SerialPort.DATABITS_7;
                    case 8: dataBits = SerialPort.DATABITS_8;
                }
            }
        });
        dataBits = SerialPort.DATABITS_8;
        stopBitsBox = new JComboBox( new String[] {"1", "2", "1.5"});
        stopBitsBox.setSelectedItem("1");
        stopBitsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = stopBitsBox.getSelectedIndex();
                switch (index) {
                    case 0: stopBits = SerialPort.STOPBITS_1;
                    case 1: stopBits = SerialPort.STOPBITS_2;
                    case 2: stopBits = SerialPort.STOPBITS_1_5;
                }
            }
        });
        stopBits = SerialPort.STOPBITS_1;
        parityBitBox = new JComboBox( new String[] {"NONE", "ODD", "EVEN", "MARK", "SPACE"});
        parityBitBox.setSelectedItem("NONE");
        parityBitBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = stopBitsBox.getSelectedIndex();
                switch (index) {
                    case 0: parity = SerialPort.PARITY_NONE;
                    case 1: parity = SerialPort.PARITY_ODD;
                    case 2: parity = SerialPort.PARITY_EVEN;
                    case 3: parity = SerialPort.PARITY_MARK;
                    case 4: parity = SerialPort.PARITY_SPACE;
                }
            }});
        parity = SerialPort.PARITY_NONE;



        startDataRead = new JButton("Start");
        startDataRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(firstStart){
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
                readingStatus.setText("Reading Status: Reading from " + comPortName);
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
                if(saveDataFile != null) startDataRead.setEnabled(true);
                try {
                    comReader.connectTo(comPortName, baud, dataBits, stopBits, parity);
                    //comReader.connectTo(comPortName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initializePlayBackButtons() {

        seekGoTo = new JButton("Go To:");
        seekGoTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.valueOf(seekField.getText());
                    if(value < seekSlider.getMaximum() && value > 0){
                        userSeek = false;
                        seekSlider.setValue(value);
                        seekTime();
                    }
                } catch (Exception ex) {seekField.setText("");}
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

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    operatingMode = OperatingMode.FROM_FILE;
                    dataFile = fileChooser.getSelectedFile();

                    if(dataBank != null) dataBank.clear();
                    else dataBank = new StaticDataBank();
                    loadFileLabel.setText("Loaded: " + dataFile.getName());
                    loadFileLabel.setOpaque(true);
                    try {
                        DataPopulator populator = new DataPopulator(dataBank, new FileReader(dataFile));
                        populator.execute();
                        int dataBankSize = dataBank.getSize();
                        seekSlider.setMaximum(dataBankSize);
                        seekSlider.setMajorTickSpacing(dataBankSize / 20);
                        seekSlider.setMinorTickSpacing(dataBankSize / 200);
                        int tickSize = (int)Math.ceil(Math.log10(dataBankSize/20));
                        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
                        for (int ii = 0; ii <= 20; ii++) {
                            labelTable.put(dataBankSize/20 * ii, new JLabel(String.format("%.1f",((float)dataBankSize)/20/(Math.pow(10, tickSize)) * ii) ));
                        }   //@TODO put some sort of modifer here to length of data file
                        seekSlider.setLabelTable(labelTable);
                        loadFileLabel.setBackground(Color.GREEN);
                        setEnabledForCOMObjects(false);
                    } catch (Exception e) {e.printStackTrace();loadFileLabel.setBackground(Color.RED);}

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

//        loadData.setVisible(false);

        startPlayBack = new JButton("Start");
        startPlayBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!started) {
                    updater = new UpdateTimer(1000/30, colorMap, plotPanel, dataBank);
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
                if( userSeek) {
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
                if (value < seekSlider.getMaximum()-1-moveAmount){
                    seekSlider.setValue(value + moveAmount);
                } else {
                    seekSlider.setValue(seekSlider.getMaximum()-1);
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

    private void seekTime() {
        int value;
        value = seekSlider.getValue();
        int begin = value - 50;
        if (begin < 0)  begin = 0;
        dataBank.resetTo(value);
        List<DataLine> dataLineList = dataBank.getPoints(begin, value);
        plotPanel.resetPlotsTo(dataLineList, value);
        colorMap.updateImage(dataBank.getPoint(value));
    }

    private JMenu tools;
    private JMenu setup;
    private JMenuItem validate;
    
    /**
     * Add the Menu Bar to the Application.
     */
    private void addMenuBar() {
        if( onMac() ) {
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

        if( onMac() ){
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
                DataPopulator populator = new DataPopulator(dataBank, new FileReader(dataFile));
                populator.execute();
                break;
            case FROM_COM_PORT:
                dataTimer = new DataTimer(comReader, fileSaveStream);
                dataTimer.setPlotPanel(plotPanel);
                dataTimer.setImage(colorMap);
                break;
        }
        //testBank();
    }

    private void testBank() {
        System.out.print(dataBank.getSize());
    }



    /**
     * Maximizes the windows and set the UI to be visible.
     */
    public void run() {
        if (application != null) {
            application.setExtendedState(JFrame.MAXIMIZED_BOTH);

            application.setVisible(true);
            // application.setResizable(false);


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

    public void reportFinish() {
        startPlayBack.setEnabled(true);
        stopPlayBack.setEnabled(false);
        seekSlider.setEnabled(true);
        back.setEnabled(true);
        next.setEnabled(true);
    }

    public void setUserSeek(boolean seek){
        userSeek = seek;
    }
}
