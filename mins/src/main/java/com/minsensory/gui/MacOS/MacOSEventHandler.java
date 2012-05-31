/* **************************************************************************
 * Copyright (C) 2011
 * Benjamin Duong, ECE @ Boston University
  *
 * All rights reserved.
 * ************************************************************************** */

 package com.minsensory.gui.MacOS;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent;
import com.apple.eawt.Application;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import com.minsensory.gui.AboutDialog;

/**
 * The <code>MacOSEventHandler</code> is use to handle the changes to the menubar
 * when running the application on a OSX platform.
 *
 * The handler provides overrides to Apple functions for when the user clicks
 * the about application or quit application options.
 */
public class MacOSEventHandler extends Application{

    public MacOSEventHandler() {
        setAboutHandler(new AboutBoxHandler());
        setQuitHandler(new ExitHandler());
    }
    class AboutBoxHandler implements AboutHandler{
        @Override
        public void handleAbout(AppEvent.AboutEvent aboutEvent) {
            new AboutDialog();
        }
    }

    class ExitHandler implements QuitHandler {

        @Override
        public void handleQuitRequestWith(AppEvent.QuitEvent quitEvent,
                                          QuitResponse quitResponse) {
            System.exit(0);
        }
    }

}
