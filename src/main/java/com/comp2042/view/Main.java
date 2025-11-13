package com.comp2042.view;

import com.comp2042.GameConfig;
import com.comp2042.controller.GameController;
import com.comp2042.model.Difficulty;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Main extends Application {

    // field for store Stage
    private Stage primaryStage;

    private MediaPlayer clearRowSoundPlayer;
    private MediaPlayer speedUpSoundPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        primaryStage.setTitle("TetrisJFX");

        loadSounds();

        showMainMenuScreen();
    }

    private void loadSounds() {
        try {
            URL clearResource = getClass().getResource("/sounds/clearRowSound.mp3");
            URL speedResource = getClass().getResource("/sounds/speedUpSound.mp3");

            if (clearResource != null) {
                Media clearMedia = new Media(clearResource.toExternalForm());
                clearRowSoundPlayer = new MediaPlayer(clearMedia);
            }
            if (speedResource != null) {
                Media speedMedia = new Media(speedResource.toExternalForm());
                speedUpSoundPlayer = new MediaPlayer(speedMedia);
            }
        } catch (Exception e) {
            System.err.println("Failed to load douns: " + e.getMessage());
        }
    }

    public void showMainMenuScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("menu.fxml"));
            Parent root = fxmlLoader.load();

            // pass the reference of the Main class to MainMenuController
            MainMenuController controller = fxmlLoader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Loads and displays the main game scene.
     * This method is called by MainMenuController.
     *
     * @param difficulty The difficulty level chosen from the menu.
     */
    public void showGameScreen(Difficulty difficulty) {
        // C:\Users\ib092\Desktop\CW2025\src\main\java\com\comp2042\view\MainMenuController.java:24:31
        // java: unreported exception java.lang.Exception; must be caught or declared to be thrown
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent root = fxmlLoader.load();
            GuiController c = fxmlLoader.getController();
            c.setMainApp(this);
            primaryStage.setScene(new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT));
            // handles selected difficulty (pass Difficulty Enum to GameController)
            new GameController(c, difficulty, clearRowSoundPlayer, speedUpSoundPlayer);
        } catch (IOException e) { e.printStackTrace(); }

    }


    public void showSettingScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("settingScreen.fxml"));
            Parent root = loader.load();
            SettingController controller = loader.getController();
            // pass the both sounds
            controller.setupVolumeControls(clearRowSoundPlayer, speedUpSoundPlayer);

            controller.setMainApp(this);

            primaryStage.setScene(new Scene(root, 420, 510));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
