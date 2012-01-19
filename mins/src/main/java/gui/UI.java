package gui;

import data.DataBank;
import data.DataCollector;
import gui.MacOS.MacOSEventHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class UI {

    private int windowWidth;
    private int windowHeight;

    public UI() {
        windowWidth = 1920;
        windowHeight = 1200;
    }

    public UI(int width, int height) {
        windowWidth = width;
        windowHeight = height;
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
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setVisible(false);

        GroupLayout layout = new GroupLayout(application.getContentPane());
        application.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        addMenuBar();
        final PlotPanel plotPanel = new PlotPanel();
        plotPanel.setPreferredSize(new Dimension(windowWidth / 4, windowHeight));
        final ColorMappedImage colorMap = new ColorMappedImage(16,16);
        colorMap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int xScale = colorMap.getWidth()/16;
                int yScale = colorMap.getHeight()/16;
                int x = mouseEvent.getX()/xScale + 1;
                int y = mouseEvent.getY()/yScale;
                int number = y*16 + x;
                if (number >256) {
                    number-=16;
                }
                plotPanel.changePlot(number);
            }
        });
        
        Random random = new Random();
        int [] colors = new int[256];
        for (int i = 0; i <256; i++) {
           colors[i] = random.nextInt();
        }
        colorMap.setColors(colors);
        colorMap.setPreferredSize(new Dimension(windowWidth / 4,
          windowWidth / 4));
        colorMap.setMaximumSize(new Dimension(windowWidth, windowHeight));

        defineLayoutPositions(layout, plotPanel, colorMap);

        setUIComponentNames();
    }

    private void addMenuBar() {
        if( onMac() ) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        menuBar = new JMenuBar();
        file = new JMenu("File");
        file.setMnemonic('F');
        load = new JMenu("Load");
        load.setMnemonic('L');
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

    private void defineLayoutPositions(GroupLayout layout, PlotPanel plotPanel,
                                       ColorMappedImage colorMap) {
        layout.setHorizontalGroup(layout.createSequentialGroup()
          .addGap(100)
          .addComponent(colorMap)
          .addGap(100)
          .addComponent(plotPanel));

        layout.setVerticalGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(
              layout.createSequentialGroup().addGap(200).addComponent(colorMap)
                .addGap(200))
            .addComponent(plotPanel)));
    }

    private void startDataCollection() {
        dataBank = new DataBank();
        dataCollector = new DataCollector(dataBank);
    }

    public void run() {
        if (application != null)

            application.setVisible(true);
    }

    private boolean onMac() {
        return (System.getProperty("mrj.version") != null);
    }

}
