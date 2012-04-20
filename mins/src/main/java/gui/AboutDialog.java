package gui;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JFrame {

    public AboutDialog(){
        super();
        setTitle("About");
        createDialog();
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
    
    private void createDialog() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        ImageIcon icon = new ImageIcon(AboutDialog.class.getResource("/TaskbarIcon.png"));
        JLabel imageIcon = new JLabel(icon);
        imageIcon.setPreferredSize(new Dimension(256, 256));
        add(imageIcon);
        setIconImage(icon.getImage());

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("MINSensory", JLabel.CENTER);
        JLabel version = new JLabel("Version 1.0", JLabel.CENTER);
        String about = "Created by \n\n Ben Duong \n Nima Haghighi-Mood \n Michael Kasparian \n\n ECE @ BU 2012";
        JTextArea aboutText = new JTextArea(about);
        aboutText.setEditable(false);
        Font font = new Font("Arial", Font.BOLD, 12);
        aboutText.setFont(font);
        aboutText.setForeground(Color.GRAY);
        JScrollPane aboutPane = new JScrollPane(aboutText);

        textPanel.add(title);
        textPanel.add(version);
        textPanel.add(aboutPane);
        textPanel.setPreferredSize(new Dimension(256, 256));
        add(textPanel);
    }

}
