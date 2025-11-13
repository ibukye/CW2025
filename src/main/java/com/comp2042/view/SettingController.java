package com.comp2042.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    @FXML
    private Slider clearRowVolumeSlider;
    @FXML
    private Slider speedUpVolumeSlider;

    private Main mainApp;
    private MediaPlayer clearRowPlayer;
    private MediaPlayer speedUpPlayer;

    // This method will be called automatically
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        clearRowVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (clearRowPlayer != null) {
                // convert the slider value to sound volume
                clearRowPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });

        speedUpVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (speedUpPlayer != null) {
                speedUpPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });
    }

    public void setupVolumeControls(MediaPlayer clearRowPlayer, MediaPlayer speedUpPlayer) {
        this.clearRowPlayer = clearRowPlayer;
        this.speedUpPlayer = speedUpPlayer;
        if (clearRowPlayer != null) {
            clearRowVolumeSlider.setValue(this.clearRowPlayer.getVolume() * 100.0);
        }
        if (speedUpPlayer != null) {
            speedUpVolumeSlider.setValue(this.speedUpPlayer.getVolume() * 100.0);
        }
    }

    @FXML
    private void goToMainMenu() {
        mainApp.showMainMenuScreen();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
