package frontend;

import gui.OperatingMode;
import gui.UI;

/**
 * Created by IntelliJ IDEA. User: bduong Date: 1/2/12 Time: 6:43 PM To change
 * this template use File | Settings | File Templates.
 */
public class MINSensory {

    public static void main(String [] args) {
        UI gui = new UI(OperatingMode.FROM_COM_PORT);
        gui.init();
        gui.run();
    }
}
