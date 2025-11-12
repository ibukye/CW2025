package com.comp2042.view;

import com.comp2042.GameConfig;
import com.comp2042.controller.InputHandler;
import com.comp2042.controller.MoveEvent;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import javax.swing.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The {@code GuiController} class handles all GUI interactions
 * It is responsible for rendering bricks, detecting user input,
 * refreshing the game view, and managing visual effects and state transitions
 * such as pause and game over.
 *
 * This controller is associated with {@code gameLayout.fxml} and interacts with
 * {@link com.comp2042.controller.GameController} to send and receive game events.
 */
public class GuiController implements Initializable {
    //private static final int BRICK_SIZE = 20;
    @FXML
    private GridPane gamePanel;
    @FXML
    private Group groupNotification;
    @FXML
    private GridPane brickPanel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label highScoreLabel;
    @FXML
    private GridPane nextBrickPanel;
    @FXML
    private Button pauseButton;
    @FXML
    private Button restartButton;
    @FXML
    private Button goBackToMenuButton;
    @FXML
    private GameOverPanel gameOverPanel;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] nextBrickRectangles;
    private InputEventListener eventListener;
    private Rectangle[][] rectangles;
    private Main mainApp;

    private Image pauseImg;
    private Image resumeImg;
    private Image restartImg;
    private ImageView pauseIconView;
    private ImageView resumeIconView;

    // MediaPlayer
    private MediaPlayer clearRowSoundPlayer;
    private MediaPlayer speedUpSoundPlayer;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    /**
     * Initializes the GUI controller, sets up keyboard input,
     * and configures the reflection effect and fonts.
     *
     * @param location  the location used to resolve relative paths for the root object.
     * @param resources the resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        // Load assets
        try {
            pauseImg = new Image(getClass().getResourceAsStream("/icons/pauseButton.png"));
            resumeImg = new Image(getClass().getResourceAsStream("/icons/resumeButton.png"));
            restartImg = new Image(getClass().getResourceAsStream("/icons/restartButton.png"));
            pauseIconView = new ImageView(pauseImg);
            resumeIconView = new ImageView(resumeImg);
            ImageView restartIconView = new ImageView(restartImg);
            pauseIconView.setFitWidth(25);
            resumeIconView.setFitWidth(25);
            restartIconView.setFitWidth(25);
            pauseIconView.setPreserveRatio(true);
            resumeIconView.setPreserveRatio(true);
            restartIconView.setPreserveRatio(true);
            pauseButton.setGraphic(pauseIconView);
            restartButton.setGraphic(restartIconView);
        } catch (Exception e) {
            System.err.println("Failed to load icon img: " + e.getMessage());
            pauseButton.setText("Pause");
            restartButton.setText("Restart");
        }

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        // InputHandler
        InputHandler inputHandler = new InputHandler(this, this.eventListener);
        gamePanel.setOnKeyPressed(inputHandler);

        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    /**
     * receives MediaPlayer from GameController
     * @param clearRowSoundPlayer Line Clear Sound
     * @param speedUpSoundPlayer Speed Up Sound
     */
    public void setupSoundPlayers(MediaPlayer clearRowSoundPlayer, MediaPlayer speedUpSoundPlayer) {
        this.clearRowSoundPlayer = clearRowSoundPlayer;
        this.speedUpSoundPlayer = speedUpSoundPlayer;
    }

    /**
     * play the MediaPlayer
     * @param player MediaPlayer
     */
    public void playSound(MediaPlayer player) {
        // Stop the previous media
        player.stop();
        player.play();
    }

    /**
     * Set the reference to the main application.
     *
     * @param mainApp Instance of Main Application
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes the game board and brick display on the GUI.
     *
     * @param boardMatrix the logical board data matrix.
     * @param brick       the view data of the current falling brick.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        // nextBrick panel initialization (4x4)
        nextBrickRectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(12, 12);
                nextBrickRectangles[i][j] = rectangle;
                // Place to the panel (component, x, y)
                nextBrickPanel.add(rectangle, j, i);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * GameConfig.BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * GameConfig.BRICK_SIZE);
    }

    private void displayNextBrick(int[][] nextBrick) {
        // need to initialize the panel to not overwrite
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                nextBrickRectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }
        // set the next brick
        for (int i = 0; i < nextBrick.length; i++) {
            for (int j = 0; j < nextBrick[i].length; j++) {
                if (nextBrick[i][j] != 0) setRectangleData(nextBrick[i][j], nextBrickRectangles[i][j]);
            }
        }
    }

    /**
     * Converts an integer color code into a {@link Paint} object.
     *
     * @param i color index (0–7).
     * @return the corresponding {@link Paint} color.
     */
    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;

        };
    }

    /**
     * Updates the position and color of the currently falling brick.
     *
     * @param brick the updated brick view data.
     */
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * GameConfig.BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * GameConfig.BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            // call next brick display method
            displayNextBrick(brick.getNextBrickData());
        }
    }

    /**
     * Refreshes the static background (merged bricks) of the game.
     *
     * @param board the matrix representing the fixed blocks on the board.
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    /**
     * Sets the color and rounded corner style for a rectangle.
     *
     * @param color     color code of the brick.
     * @param rectangle the target rectangle to modify.
     */
    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    /**
     * Moves the current brick down one step and updates the view.
     * If a row is cleared, a floating score notification is shown.
     *
     * @param event the downward move event.
     */
    // changed from private to public to use in InputHandler
    public void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                //NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                //groupNotification.getChildren().add(notificationPanel);
                //notificationPanel.showScore(groupNotification.getChildren());
                showNotification("+" + downData.getClearRow().getScoreBonus(), 0);
                playSound(clearRowSoundPlayer);
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void handleHardDrop() {
        // ask Controller to run
        DownData downData = eventListener.onHardDropEvent();
        // Same as moveDown
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            //NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            //groupNotification.getChildren().add(notificationPanel);
            //notificationPanel.showScore(groupNotification.getChildren());
            showNotification("+" + downData.getClearRow().getScoreBonus(), 0);
            playSound(clearRowSoundPlayer);
        }
        refreshBrick(downData.getViewData());
        gamePanel.requestFocus();
    }

    /**
     * To display a generalized notification panel
     * @param text text to display on the notification
     * @param yOffset offset for y (0 = bonus score, 30 = speed up)
     */
    public void showNotification(String text, double yOffset) {
        NotificationPanel speedUpPanel = new NotificationPanel(text);
        // move Y coordinates
        speedUpPanel.setLayoutY(yOffset);
        groupNotification.getChildren().add(speedUpPanel);
        speedUpPanel.showScore(groupNotification.getChildren());
    }

    /**
     * Sets the input event listener for the GUI controller.
     *
     * @param eventListener the listener implementing {@link InputEventListener}.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        InputHandler inputHandler = new InputHandler(this, this.eventListener);
        gamePanel.setOnKeyPressed(inputHandler);
    }

    /**
     * Binds the displayed score to the game’s score property.
     * This method links the score value from the Model to the scoreLabel in the View.
     *
     * @param integerProperty the score property from the Score object (Model).
     */
    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString());
    }

    /**
     * Updates the High Score label with new high score.
     * @param score New high score.
     */
    public void updateHighScore(int score) {
        highScoreLabel.setText(String.valueOf(score));
    }

    /**
     * Displays the game over panel and stops the game.
     */
    public void gameOver() {
        //timeLine.stop();
        //pauseGame(null);
        //eventListener.stopGame();   // call from interface (Separation of Concerns)
        // Ask for save score to GameController (eventListener)
        eventListener.saveGameScore();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    /**
     * Starts a new game and resets the game state.
     *
     * @param actionEvent the event triggering the new game.
     */
    public void newGame(ActionEvent actionEvent) {
        //timeLine.stop();
        //eventListener.stopGame();   // Chamged from timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        //timeLine.play();
        //eventListener.resumeGame();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        pauseButton.setGraphic(pauseIconView);
    }

    /**
     * Toggles between pause and resume states.
     * Updates the pauseButton text accordingly.
     *
     * @param actionEvent the event triggering the pause/resume action.
     */
    public void pauseGame(ActionEvent actionEvent) {
        if (isPause.getValue() == Boolean.FALSE) {
            // if playing -> pause
            eventListener.stopGame();
            isPause.setValue(Boolean.TRUE);
            pauseButton.setGraphic(resumeIconView);
        } else {
            // if pause -> resume
            eventListener.resumeGame();
            isPause.setValue(Boolean.FALSE);
            pauseButton.setGraphic(pauseIconView);

        }
        gamePanel.requestFocus();
    }

    /**
     * Gets the pause state.
     * @return the isPause Boolean
     */
    public boolean isPause() { return isPause.get(); }

    /**
     * Gets the game over state.
     * @return the isGameOver Boolean
     */
    public boolean isGameOver() { return isGameOver.get(); }

    /**
     * Gets the event listener (GameController).
     * @return the InputEventListener
     */
    public InputEventListener getEventListener() { return this.eventListener; }


    /**
     * Handles the "Back to Menu" button action.
     * Shows a confirmation dialog before stopping the game and returning to the main menu.
     *
     * @param actionEvent The event triggering this action.
     */
    @FXML
    private void goBackToMenu(ActionEvent actionEvent) {
        // Need to stop game when the button is pressed
        eventListener.stopGame();
        isPause.setValue(Boolean.TRUE);
        // Show the pop-up
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to go back to menu?");
        // Wait for user input
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            // OK is pressed -> go to main menu
            mainApp.showMainMenuScreen();
        } else {
            // No -> resume the game
            eventListener.resumeGame();
            isPause.setValue(Boolean.FALSE);
            gamePanel.requestFocus();   // returns the focus to the game panel
        }
    }


}
