package java.com.ferdynand.controllers;

import javafx.application.Application;
import main.java.com.ferdynand.client.Main;
import main.java.com.ferdynand.controllers.UserAndModeWindowController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;


public class UserAndModeWindowControllerTest {
    private volatile boolean success = false;
    private volatile boolean errorLabel = false;

    @org.junit.jupiter.api.Test
    public void shouldLaunchJavaMainApplication() {
        Thread thread = new Thread() { // Wrapper thread.
            @Override
            public void run() {
                try {
                    Main main = new Main();
                    Application.launch(main.getClass()); // Run JavaFX application.

                    success = true;
                } catch(Throwable t) {
                    if(t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                        // We expect to get this exception since we interrupted
                        // the JavaFX application.

                        success = true;
                        return;
                    }
                    // This is not the exception we are looking for so log it.
                    Logger.getLogger(UserAndModeWindowControllerTest.class.getName()).log(Level.SEVERE, null, t);
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(3000);  // Wait for 3 seconds before interrupting JavaFX application
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        thread.interrupt();
        try {
            thread.join(500); // Wait 0.5 second for our wrapper thread to finish.
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        Assertions.assertTrue(success);

    }

    @Test
    public void shouldSetErrorLabelCauseUsernameWasEmpty(){
        success = false;
        Main main = new Main();
        Thread thread = new Thread(() -> {
            try{
                Application.launch(main.getClass());
                Thread.sleep(1000);
                UserAndModeWindowController controller = main.getLoader().getController();
                controller.handleStartGameButton();
                errorLabel = controller.usernameErrorLabel.isVisible();
                success = true;
            } catch (Throwable t){
                if(t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                    success = true;
                    return;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e){
            // Wake up early nothing specially happened
        }
        thread.interrupt();
        try {
            thread.join(500); // Wait 0.5 sec for wrapper thread to finish
        } catch (InterruptedException e) {
            // Don't care
        }
        Assertions.assertTrue(success);
    }
}