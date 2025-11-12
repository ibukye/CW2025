package com.comp2042.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

// change to VBox to align label and button vertically
public class GameOverPanel extends VBox {

    private Runnable onMainMenu;    // field to save action

    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        final Button mainMenuButton = new Button("Main Menu");
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(gameOverLabel, mainMenuButton);

        mainMenuButton.setOnAction(event -> {
            onMainMenu.run();
        });


        //setCenter(gameOverLabel);
    }

    /**
     *
     * @param action
     */
    public void setMainMenu(Runnable action) {
        this.onMainMenu = action;
    }

}
