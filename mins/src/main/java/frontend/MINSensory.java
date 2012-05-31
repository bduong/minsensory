/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package frontend;

import gui.OperatingMode;
import gui.UI;

/**
 *The <code>MINSensory</code> object gives a hook to start the application.
 *
 * It initializes a new UI and runs it to display to the user.
*/
public class MINSensory {
    public static void main(String [] args) {
        UI gui = new UI();
        gui.init();
        gui.run();
    }
}
