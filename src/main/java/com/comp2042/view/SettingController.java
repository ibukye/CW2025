package com.comp2042.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the settings screen (settingScreen.fxml).
 * This class manages the UI elements on the settings screen, primarily
 * sliders for adjusting the volume of the game's shared {@link MediaPlayer} instances.
 * It receives the {@link Main} app reference for navigation and the players
 * for volume control.
 */
public class SettingController implements Initializable {

    /** The FXML {@link Slider} that controls the volume. */
    @FXML
    private Slider clearRowVolumeSlider;
    @FXML
    private Slider speedUpVolumeSlider;

    /** A reference to the main application class for switching scenes. */
    private Main mainApp;

    /** The shared {@link MediaPlayer} for the row clear sound and speed up sound, received from Main. */
    private MediaPlayer clearRowPlayer;
    private MediaPlayer speedUpPlayer;

    /**
     * Initializes the controller.
     * This method is called automatically by JavaFX after the FXML file is loaded.
     * It adds listeners to both volume sliders, which update the volume
     * of the corresponding {@link MediaPlayer} in real-time.
     *
     * @param url The location used to resolve relative paths for the root object, or null if not known.
     * @param resourceBundle The resources used to localize the root object, or null if not known.
     */
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

    /**
     * Injects the shared {@link MediaPlayer} instances from the {@link Main} application.
     * This method also sets the initial value of the sliders to match the
     * players' current volume levels.
     *
     * @param clearRowPlayer The shared player for the line clear sound.
     * @param speedUpPlayer  The shared player for the speed up sound.
     */
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

    /**
     * Handles the "Back" button click event.
     * Tells the main application to navigate back to the main menu screen.
     */
    @FXML
    private void goToMainMenu() {
        mainApp.showMainMenuScreen();
    }

    /**
     * Sets the reference to the main application class.
     * This is used for scene switching (e.g., returning to the main menu).
     *
     * @param mainApp The instance of the {@link Main} application.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
