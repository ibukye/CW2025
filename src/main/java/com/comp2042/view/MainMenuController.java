package com.comp2042.view;

import com.comp2042.model.Difficulty;
import javafx.fxml.FXML;
//import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;

/**
 * The controller for the main menu screen (menu.fxml).
 * This class handles user interactions on the main menu, such as
 * selecting a difficulty to start the game, opening the settings screen,
 * or exiting the application. It communicates back to the {@link Main}
 * application class to manage scene transitions.
 */
public class MainMenuController {

    /** A reference to the main application class for switching scenes. */
    // field which have the reference to the Main Class
    private Main mainApp;

    /**
     * Sets the reference to the main application.
     * This method is called by the {@link Main} class after loading the FXML
     * to enable this controller to call back for scene changes.
     *
     * @param mainApp The instance of the {@link Main} application.
     */
    // receives reference from Main class
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Handles the "Easy" button click event.
     * Tells the main application to start the game with {@link Difficulty#EASY}.
     *
     * @param e The ActionEvent triggered by the button.
     */
    // Easy mode
    @FXML
    void onEasyClicked(ActionEvent e) {
        // create Main class with easy mode
        mainApp.showGameScreen(Difficulty.EASY);
    }

    /**
     * Handles the "Normal" button click event.
     * Tells the main application to start the game with {@link Difficulty#NORMAL}.
     *
     * @param e The ActionEvent triggered by the button.
     */
    // Normal mode
    @FXML
    void onNormalClicked(ActionEvent e) {
        // create Main class with normal mode
        mainApp.showGameScreen(Difficulty.NORMAL);
    }

    /**
     * Handles the "Hard" button click event.
     * Tells the main application to start the game with {@link Difficulty#HARD}.
     *
     * @param e The ActionEvent triggered by the button.
     */
    // Hard mode
    @FXML
    void onHardClicked(ActionEvent e) {
        // create Main class with hard mode
        mainApp.showGameScreen(Difficulty.HARD);
    }

    /**
     * Handles the "Settings" button click event.
     * Tells the main application to display the settings screen.
     *
     * @param e The ActionEvent triggered by the button.
     */
    // Setting Button
    @FXML
    void onSettingClicked(ActionEvent e) {
        mainApp.showSettingScreen();
    }

    /**
     * Handles the "Exit" button click event.
     * Safely terminates the JavaFX application.
     *
     * @param e The ActionEvent triggered by the button.
     */
    // Exit Button
    @FXML
    void onExitClicked(ActionEvent e) { System.exit(0); }
}