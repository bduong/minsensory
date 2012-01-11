package gui.MacOS;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent;
import com.apple.eawt.Application;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import gui.AboutDialog;

import javax.swing.*;

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
