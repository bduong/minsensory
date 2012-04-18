package gui;

import data.DataBank;
import data.DataLine;
import data.playback.DataPopulator;
import data.playback.FileReader;
import data.playback.StaticDataBank;
import data.realtime.COMReader;
import data.realtime.DataCollector;
import data.realtime.DynamicDataBank;
import gnu.io.NoSuchPortException;
import gui.MacOS.MacOSEventHandler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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
    private JMenuItem load;
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
    private JButton selectPort;

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

    boolean firstStart = true;

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
        started = false;

        application = new JFrame("System For Sensing Neural Response");
        application.setSize(windowWidth, windowHeight);
        application.setMinimumSize(new Dimension(windowWidth, windowHeight));

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setVisible(false);

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

                if(f.exists() && getDialogType() == OPEN_DIALOG) {
                    Object selectedValue = JOptionPane.showInputDialog(null,
                      "Choose Data Type", "Input",
                      JOptionPane.INFORMATION_MESSAGE, null,
                      options, options[1]);
                    if (selectedValue == null) {
                        cancelSelection();
                        return;
                    } else if (selectedValue == options[0]) {
                        colorMap.setDataType(DataType.RAW);
                        plotPanel.setDataType(DataType.RAW);
                        super.approveSelection();
                        return;
                    } else {
                        colorMap.setDataType(DataType.PROCESSED);
                        plotPanel.setDataType(DataType.PROCESSED);
                        super.approveSelection();
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


        addMenuBar();

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        plotPanel = new PlotPanel();
        plotPanel.setPreferredSize(new Dimension(windowWidth / 4, windowHeight));
        colorMap = new ColorMappedImage(16,16);
        colorMap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int xScale = colorMap.getWidth()/16;
                int yScale = colorMap.getHeight()/16;
                int x = mouseEvent.getX()/xScale;
                int y = mouseEvent.getY()/yScale + 1;
                if (y > 16) {
                    y=16;
                }
                plotPanel.changePlot(y,x);
            }
        });

         //Color Plot
        int [] colors = new int[256];
        for (int i = 0; i <256; i++) {
            colors[i] = Integer.MAX_VALUE * ((i/16 % 2 + i) % 2);
        }
        colorMap.setColors(colors);
//        colorMap.setPreferredSize(new Dimension(windowWidth / 3,
//                windowHeight / 3));
//        colorMap.setMaximumSize(new Dimension(windowWidth, windowHeight));

        ColorGrid colorGrid = new ColorGrid(colorMap);
        colorGrid.setPreferredSize(new Dimension(windowWidth / 3,
                windowWidth / 3));

        /**
         * Set Layouts
         */
        layout.setHonorsVisibility(false);
        layout.setHonorsVisibility(seekSlider, true);
        layout.setHonorsVisibility(back, true);
        layout.setHonorsVisibility(next, true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
          .addGap(40)
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(colorGrid, 0, GroupLayout.PREFERRED_SIZE,
              Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(realTimeSelect, 0, GroupLayout.PREFERRED_SIZE,
                  buttonLength)
                .addComponent(startRealTimeData, 0, GroupLayout.PREFERRED_SIZE,
                  buttonLength)
              )
              .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(playBackSelect, 0, GroupLayout.PREFERRED_SIZE,
                  buttonLength)
                .addComponent(loadData, 0, GroupLayout.PREFERRED_SIZE,
                  buttonLength)
              )
            )
            .addGroup(layout.createSequentialGroup()
              .addComponent(back)
              .addComponent(seekSlider)
              .addComponent(next)
            )

          )
          .addGap(100)
          .addComponent(plotPanel, 0, GroupLayout.PREFERRED_SIZE,
            Short.MAX_VALUE));

        int vertGap = (application.getHeight()-colorGrid.getPreferredSize().height)/2;
        layout.setVerticalGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(
              layout.createSequentialGroup()
                .addComponent(colorGrid, 0, GroupLayout.PREFERRED_SIZE,
                  Short.MAX_VALUE)
                .addGap(20)
                .addGroup(
                  layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(realTimeSelect, 0, GroupLayout.PREFERRED_SIZE,
                      buttonWidth)
                    .addComponent(playBackSelect, 0, GroupLayout.PREFERRED_SIZE,
                      buttonWidth)
                )
                .addGroup(
                  layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(startRealTimeData, 0,
                      GroupLayout.PREFERRED_SIZE, buttonWidth)
                    .addComponent(loadData, 0, GroupLayout.PREFERRED_SIZE,
                      buttonWidth)
                )
                .addGroup(
                  layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(back)
                    .addComponent(seekSlider)
                    .addComponent(next)
                )
                .addGap(20)
            )
            .addComponent(plotPanel, 0, GroupLayout.PREFERRED_SIZE,
              Short.MAX_VALUE)));

        layout.linkSize(SwingConstants.HORIZONTAL, realTimeSelect, playBackSelect);
        layout.linkSize(SwingConstants.HORIZONTAL, realTimeSelect,
          startRealTimeData);
        layout.linkSize(SwingConstants.HORIZONTAL, realTimeSelect, loadData);
        layout.linkSize(SwingConstants.VERTICAL, realTimeSelect, playBackSelect);
        layout.linkSize(SwingConstants.VERTICAL, realTimeSelect,
          startRealTimeData);
        layout.linkSize(SwingConstants.VERTICAL, realTimeSelect, loadData);
        layout.linkSize(SwingConstants.VERTICAL, next, back);
        layout.linkSize(SwingConstants.HORIZONTAL, next, back);

        setUIComponentNames();
    }

    private void initializeRealTimeButtons() {
        realTimeSelect = new JButton("Real Time Data");
        realTimeSelect.setPreferredSize(new Dimension(buttonLength, buttonWidth));
        realTimeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                realTimeSelect.setEnabled(false);
                playBackSelect.setEnabled(true);
                startRealTimeData.setVisible(true);
                loadData.setVisible(false);
            }
        });

        startRealTimeData = new JButton("Start");
        startRealTimeData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                operatingMode = OperatingMode.FROM_COM_PORT;
                int returnVal = fileChooser.showSaveDialog(application);

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                     saveDataFile = fileChooser.getSelectedFile();

                    layout.replace(realTimeSelect, startDataRead);
                    layout.replace(playBackSelect, stopDataRead);
                    layout.replace(startRealTimeData, selectPort);
                    layout.replace(loadData, disconnect);

                    layout.linkSize(SwingConstants.HORIZONTAL, startDataRead, stopDataRead);
                    layout.linkSize(SwingConstants.HORIZONTAL, startDataRead, selectPort);
                    layout.linkSize(SwingConstants.HORIZONTAL, startDataRead, disconnect);
                    layout.linkSize(SwingConstants.VERTICAL, startDataRead, stopDataRead);
                    layout.linkSize(SwingConstants.VERTICAL, startDataRead, selectPort);
                    layout.linkSize(SwingConstants.VERTICAL, startDataRead, disconnect);

                    startDataRead.setEnabled(false);
                    stopDataRead.setEnabled(false);
                    disconnect.setEnabled(false);
                    selectPort.setEnabled(true);
                }
            }
        });

        startRealTimeData.setVisible(false);



        startDataRead = new JButton("Start Data Stream");
        startDataRead.setPreferredSize(new Dimension(buttonLength, buttonWidth));
        startDataRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(firstStart){
                    try {
                        startDataCollection();
                        firstStart = false;
                        dataCollector.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    dataCollector.resume();
                }
                startDataRead.setEnabled(false);
                stopDataRead.setEnabled(true);
                disconnect.setEnabled(false);
                selectPort.setEnabled(false);
                SwingUtilities.invokeLater(dataCollector);
            }
        });

        stopDataRead = new JButton("Stop Data Stream");
        stopDataRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dataCollector.pause();
                startDataRead.setEnabled(true);
                stopDataRead.setEnabled(false);
                disconnect.setEnabled(true);
            }
        });

        disconnect = new JButton("Disconnect Data Stream");
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startDataRead.setEnabled(false);
                stopDataRead.setEnabled(false);
                disconnect.setEnabled(false);
                selectPort.setEnabled(true);
                dataCollectorThread = null;
            }
        });

        selectPort = new JButton("Select Port");
        selectPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Object [] ports = COMReader.listPorts().toArray();
                    Object selectedValue = JOptionPane.showInputDialog(null,
                            "Select Port", "Input",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            ports, ports[0]);
                    if (selectedValue != null) {
                        port = selectedValue.toString();
                        startDataRead.setEnabled(true);
                        disconnect.setEnabled(true);
                        selectPort.setEnabled(false);
                        System.out.println(port);
                    }
                } catch (NoSuchPortException e) {
                    e.printStackTrace();  
                }


            }
        });

    }

    private void initializePlayBackButtons() {
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

        loadData = new JButton("Load");
        loadData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int returnVal = fileChooser.showOpenDialog(application);

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    dataFile = fileChooser.getSelectedFile();
                    loadData.setVisible(false);
                    playBackSelect.setVisible(false);
                    realTimeSelect.setVisible(false);

                    dataBank = new StaticDataBank();
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

                    } catch (Exception e) {}

                    layout.replace(realTimeSelect, startPlayBack);
                    layout.replace(playBackSelect, stopPlayBack);
                    stopPlayBack.setEnabled(false);

                    layout.linkSize(SwingConstants.HORIZONTAL, startPlayBack, stopPlayBack);
                    layout.linkSize(SwingConstants.VERTICAL, startPlayBack, stopPlayBack);

                    layout.setHonorsVisibility(startRealTimeData, true);
                    layout.setHonorsVisibility(loadData, true);
                    seekSlider.setVisible(true);
                    back.setVisible(true);
                    next.setVisible(true);
                }
            }
        }
        );

        loadData.setVisible(false);

        startPlayBack = new JButton("Start PlayBack");
        startPlayBack.setPreferredSize(new Dimension(buttonLength, buttonWidth));
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
                updater.startTimer();
                startPlayBack.setEnabled(false);
                stopPlayBack.setEnabled(true);

            }
        });
        stopPlayBack = new JButton("Stop PlayBack");
        stopPlayBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updater.stopTimer();
                startPlayBack.setEnabled(true);
                stopPlayBack.setEnabled(false);
                seekSlider.setEnabled(true);
                back.setEnabled(true);
                next.setEnabled(true);
            }
        });

        int backButtonLength = 20;
        seekSlider = new JSlider(JSlider.HORIZONTAL, 0, 1, 0);
        seekSlider.setPreferredSize(new Dimension(buttonLength, buttonWidth));
        seekSlider.setVisible(false);
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
        next.setVisible(false);
        next.setPreferredSize(new Dimension(backButtonLength, buttonWidth));
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
        back.setVisible(false);
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
        load = new JMenuItem("Load");
        load.setMnemonic('L');
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int returnVal = fileChooser.showOpenDialog(application);

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    dataFile = fileChooser.getSelectedFile();
                    try {
                        startDataCollection();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
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



        if( onMac() ){
            new MacOSEventHandler();
        } else {
            file.add(exit);
            help.add(about);
        }
        file.add(load);

        menuBar.add(file);
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
            dataBank = new DynamicDataBank();
            COMReader comReader = new COMReader();
            comReader.connectTo(port);
            dataCollector = new DataCollector(dataBank, comReader , saveDataFile);
            break;
        }
        testBank();
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
