package com.comp2042.view;

import com.comp2042.GameConfig;
import com.comp2042.controller.GameController;
import com.comp2042.model.Difficulty;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        primaryStage.setTitle("TetrisJFX");

        showMainMenuScreen();
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
            primaryStage.setScene(new Scene(root));
            // handles selected difficulty (pass Difficulty Enum to GameController)
            new GameController(c, difficulty);
        } catch (IOException e) { e.printStackTrace(); }

    }




    public static void main(String[] args) {
        launch(args);
    }
}
