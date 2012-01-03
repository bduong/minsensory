package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class UI {

    private int windowWidth;
    private int windowHeight;

    public UI () {
        windowWidth = 600;
        windowHeight = 400;
    }

    public UI (int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    private JFrame application;

    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem exit;
    private JMenu help;
    private JMenuItem about;



    public void init() {
        application = new JFrame("System For Sensing Neural Response");
        application.setSize(windowWidth, windowHeight);
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setVisible(false);

        menuBar = new JMenuBar();
        file = new JMenu("File");
        file.setMnemonic('F');
        exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        file.add(exit);

        help = new JMenu("Help");
        help.setMnemonic('H');
        about = new JMenuItem("About");
        about.setMnemonic('A');
        help.add(about);

        menuBar.add(file);
        menuBar.add(help);

        application.setJMenuBar(menuBar);



        ImageIcon imageIcon = new ImageIcon(UI.class.getResource("/MINSensory Logo.jpg"));
        application.setIconImage(imageIcon.getImage());

    }

    public void run() {
        if (application != null)
        application.setVisible(true);
    }


}
