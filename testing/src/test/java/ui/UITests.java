package ui;


import gui.menu.items.ValidationTestPanel;

import javax.swing.*;
import java.awt.*;

public class UITests {
    
    
    public static void main(String args []) {
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
//        frame.add(new SelectionPanel().getPlayOptions(), BorderLayout.CENTER);
        frame.add(new ValidationTestPanel(), BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
