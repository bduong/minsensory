import com.apple.eawt.Application;
import gui.UI;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: bduong Date: 1/2/12 Time: 6:43 PM To change
 * this template use File | Settings | File Templates.
 */
public class MINSensory {

    public static void main(String [] args) {
        ImageIcon imageIcon = new ImageIcon(UI.class.getResource("/MINSensory Logo.jpg"));
        UI gui = new UI();
        gui.init();
        gui.run();
    }
}
