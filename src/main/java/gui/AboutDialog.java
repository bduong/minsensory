package gui;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JFrame {

    public AboutDialog(){
        super();
      createDialog();
      setVisible(true);
    }
    
    private void createDialog() {
        setLayout(new GridLayout(1,2));
        Icon icon = new ImageIcon(AboutDialog.class.getResource("/MINSensory Logo.jpg"));
        JLabel imageIcon = new JLabel(icon);
        imageIcon.setPreferredSize(new Dimension(50, 50));
        add(imageIcon);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("MINSensory");
        JLabel version = new JLabel("Version 1.0");
        String about = "Created by \n Ben Duong \n Nima Haghighi-Modd \n Mike Kasparian \n\n Boston University 2012";
        JTextArea aboutText = new JTextArea(about);
        aboutText.setEditable(false);
        JScrollPane aboutPane = new JScrollPane(aboutText);

        textPanel.add(title);
        textPanel.add(version);
        textPanel.add(aboutPane);
        add(textPanel);
    }

}
