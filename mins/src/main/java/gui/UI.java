package gui;

import data.playback.DataPopulator;
import data.playback.FileReader;
import data.playback.StaticDataBank;
import data.realtime.COMReader;
import data.DataBank;
import data.realtime.DynamicDataBank;
import data.realtime.DataCollector;
import gui.MacOS.MacOSEventHandler;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

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
        application = new JFrame("System For Sensing Neural Response");
        application.setSize(windowWidth, windowHeight);
        application.setMinimumSize(new Dimension(windowWidth, windowHeight));

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setVisible(false);

        GroupLayout layout = new GroupLayout(application.getContentPane());
        application.setLayout(layout);

        fileChooser = new JFileChooser("Choose Data File");
        fileChooser.setMultiSelectionEnabled(false);


        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        addMenuBar();
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


        Random random = new Random();
        int [] colors = new int[256];
        for (int i = 0; i <256; i++) {
            colors[i] = random.nextInt();
        }
        colorMap.setColors(colors);
//        colorMap.setPreferredSize(new Dimension(windowWidth / 3,
//                windowHeight / 3));
//        colorMap.setMaximumSize(new Dimension(windowWidth, windowHeight));

        ColorGrid colorGrid = new ColorGrid(colorMap);
        colorGrid.setPreferredSize(new Dimension(windowWidth / 3,
                windowWidth / 3));

        defineLayoutPositions(layout, plotPanel, colorGrid);

//        application.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                windowWidth = application.getWidth();
//                windowHeight = application.getHeight();
//                
//                System.out.println(windowWidth);
//                System.out.println(windowHeight);
//                colorMap.setPreferredSize(new Dimension(windowWidth / 3,
//                       windowWidth / 3));
//                colorMap.repaint();
//                //colorMap.setSize(new Dimension(windowWidth / 3,
//                //     windowWidth / 3));
//                //colorMap.setMaximumSize(new Dimension(windowWidth, windowHeight));
//                System.out.println(colorMap.getSize());
//                plotPanel.setPreferredSize(new Dimension(windowWidth / 4, windowHeight));
//            }
//        });

        setUIComponentNames();
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
     * Defines the positions for the Group Layout
     * 
     * @param layout the group layout
     * @param plotPanel the JPanel with the plots
     * @param colorMap the JPanel with the color grid
     */
    private void defineLayoutPositions(GroupLayout layout, PlotPanel plotPanel,
                                       ColorGrid colorMap) {
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(20)
                .addComponent(colorMap, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                .addGap(200)
                .addComponent(plotPanel, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE));

        int vertGap = (application.getHeight()-colorMap.getPreferredSize().height)/2;
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(
                layout.createSequentialGroup()
                            .addGap(vertGap)                    
                            .addComponent(colorMap, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                            .addGap(vertGap)
                        )
                
                        .addComponent(plotPanel, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)));
    }

    /**
     * Start the collection of Data
     * 
     * @throws URISyntaxException if the file cannot be found
     * @throws IOException if the file cannot be read
     */
    private void startDataCollection() throws URISyntaxException, IOException {
        switch (operatingMode) {
        case FROM_FILE:
            dataBank = new StaticDataBank();
            DataPopulator populator = new DataPopulator(dataBank, new FileReader(dataFile));
            populator.execute();
            startPlayBack();
            break;
        case FROM_COM_PORT:
            dataBank = new DynamicDataBank();
            dataCollector = new DataCollector(dataBank, new COMReader());
            dataCollector.execute();
            break;
        }
        testBank();
    }

    private void testBank() {
        System.out.print(dataBank.getSize());
    }

    private void startPlayBack() {
        updater = new UpdateTimer(1000/30, colorMap, plotPanel, dataBank);
        updater.startTimer();
    }

    /**
     * Maximizes the windows and set the UI to be visible.
     */
    public void run() {
        if (application != null) {
            application.setExtendedState(JFrame.MAXIMIZED_BOTH);
            
            application.setVisible(true);
            application.setResizable(false);


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
}
