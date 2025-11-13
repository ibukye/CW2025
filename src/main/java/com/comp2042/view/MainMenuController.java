package com.comp2042.view;

import com.comp2042.model.Difficulty;
import javafx.fxml.FXML;
//import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;


public class MainMenuController {
    // field which have the reference to the Main Class
    private Main mainApp;

    // receives reference from Main class
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    // FXML code

    // Easy mode
    @FXML
    void onEasyClicked(ActionEvent e) {
        // create Main class with easy mode
        mainApp.showGameScreen(Difficulty.EASY);
    }

    // Normal mode
    @FXML
    void onNormalClicked(ActionEvent e) {
        // create Main class with normal mode
        mainApp.showGameScreen(Difficulty.NORMAL);
    }

    // Hard mode
    @FXML
    void onHardClicked(ActionEvent e) {
        // create Main class with hard mode
        mainApp.showGameScreen(Difficulty.HARD);
    }

    // Setting Button
    @FXML
    void onSettingClicked(ActionEvent e) {
        mainApp.showSettingScreen();
    }

    // Exit Button
    @FXML
    void onExitClicked(ActionEvent e) { System.exit(0); }
}