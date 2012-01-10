package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI {

    private int windowWidth;
    private int windowHeight;

    public UI() {
        windowWidth = 900;
        windowHeight = 600;
    }

    public UI(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

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
        file.add(load);
        file.add(exit);

        help = new JMenu("Help");
        help.setMnemonic('H');
        about = new JMenuItem("About");
        about.setMnemonic('A');
        help.add(about);

        menuBar.add(file);
        menuBar.add(help);

        application.setJMenuBar(menuBar);

        PlotPanel plotPanel = new PlotPanel();
        ColorMappedImage colorMap = new ColorMappedImage(16,16);
        colorMap.setSize(new Dimension(windowWidth/4, windowHeight/4));
        PlotPanel plotPanel1 = new PlotPanel();

        layout.setHorizontalGroup(layout.createSequentialGroup()
          .addComponent(plotPanel1)
          .addGap(300)
          .addComponent(plotPanel));

        layout.setVerticalGroup(layout.createSequentialGroup()
          .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(plotPanel1).addComponent(plotPanel)));

    }

    public void run() {
        if (application != null)

            application.setVisible(true);
    }

}
