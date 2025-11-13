package com.comp2042.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A custom JavaFX component that displays the "GAME OVER" message
 * and a "Main Menu" button.
 * This panel extends {@link VBox} to vertically align the label and the button.
 * It provides a method ({@link #setMainMenu(Runnable)}) to inject the action
 * that should be performed when the "Main Menu" button is clicked.
 */
// change to VBox to align label and button vertically
public class GameOverPanel extends VBox {

    /**
     * Stores the action (as a {@link Runnable}) to be executed when the
     * "Main Menu" button is clicked. This is typically set by the GuiController.
     */
    private Runnable onMainMenu;    // field to save action

    /**
     * Constructs the GameOverPanel.
     * Initializes the "GAME OVER" label and "Main Menu" button,
     * sets their styles and layout (centered, with spacing), and
     * configures the button's click event.
     */
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
    }

    /**
     * Sets the action to be executed when the "Main Menu" button is pressed.
     * This method is used to pass the navigation logic (e.g., {@code mainApp.showMainMenuScreen()})
     * from the {@link GuiController} into this panel.
     *
     * @param action The {@link Runnable} to execute on button click.
     */
    public void setMainMenu(Runnable action) {
        this.onMainMenu = action;
    }

}
