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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.List;

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

    /** The main grid pane that holds the static, merged bricks (the game board). */
    @FXML
    private GridPane gamePanel;
    /** The Group container for displaying notifications (e.g., "+100", "Speed UP!"). */
    @FXML
    private Group groupNotification;
    /** The grid pane that moves around to display the currently falling brick. */
    @FXML
    private GridPane brickPanel;

    /** The label used to display the current score. */
    @FXML
    private Label scoreLabel;
    /** The label used to display the persistent high score. */
    @FXML
    private Label highScoreLabel;

    /** The 4x4 grid pane for the "next" brick. */
    @FXML
    private GridPane nextBrickPanel;
    @FXML
    private GridPane nextBrickPanel2;
    @FXML
    private GridPane nextBrickPanel3;
    @FXML
    private GridPane nextBrickPanel4;

    //** The button for pausing/resuming the game. */
    @FXML
    private Button pauseButton;
    /** The button for restarting the game. */
    @FXML
    private Button restartButton;
    /** The button for returning to the main menu. */
    @FXML
    private Button goBackToMenuButton;

    /** The custom panel displayed on game over. */
    @FXML
    private GameOverPanel gameOverPanel;
    /** The 4x4 grid pane for displaying the ghost piece (drop forecast). */
    @FXML
    private GridPane ghostBrickPanel;
    /** The 4x4 grid pane for displaying the held brick. */
    @FXML
    private GridPane holdBrickPanel;


    /** 2D array holding the {@link Rectangle} objects for the main game board (displayMatrix). */
    private Rectangle[][] displayMatrix;

    /** 2D array holding the {@link Rectangle} objects for the "next" brick panel. */
    private Rectangle[][] nextBrickRectangles;
    private Rectangle[][] nextBrickRectangles2;
    private Rectangle[][] nextBrickRectangles3;
    private Rectangle[][] nextBrickRectangles4;

    /** 2D array holding the {@link Rectangle} objects for the ghost piece panel. */
    private Rectangle[][] ghostRectangles;

    /** 2D array holding the {@link Rectangle} objects for the hold piece panel. */
    private Rectangle[][] holdBrickRectangle;

    /** A reference to the Controller (implements {@link InputEventListener}). */
    private InputEventListener eventListener;

    /** 2D array holding the {@link Rectangle} objects for the currently falling brick panel. */
    private Rectangle[][] rectangles;

    /** A reference to the Main application class, used for switching scenes. */
    private Main mainApp;

    /** Cached image for buttons. */
    private Image pauseImg;
    private Image resumeImg;
    private Image restartImg;

    /** Cached {@link ImageView} for icons. */
    private ImageView pauseIconView;
    private ImageView resumeIconView;

    // MediaPlayer
    /** Shared {@link MediaPlayer} for sounds. */
    private MediaPlayer clearRowSoundPlayer;
    private MediaPlayer speedUpSoundPlayer;

    /** JavaFX property tracking the pause state. */
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    /** JavaFX property tracking the game over state. */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    /**
     * Initializes the GUI controller.
     * aThis method is called automatically by JavaFX after the FXML file is loaded.
     * It loads fonts, icons, sets up the game over panel, and requests focus.
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

        // Setup the game over panel's "Main Menu" button action
        gameOverPanel.setMainMenu(() -> {
            mainApp.showMainMenuScreen();
        });

        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    /**
     * Receives the shared {@link MediaPlayer} instances from the {@link GameController}.
     *
     * @param clearRowSoundPlayer Line Clear Sound
     * @param speedUpSoundPlayer Speed Up Sound
     */
    public void setupSoundPlayers(MediaPlayer clearRowSoundPlayer, MediaPlayer speedUpSoundPlayer) {
        this.clearRowSoundPlayer = clearRowSoundPlayer;
        this.speedUpSoundPlayer = speedUpSoundPlayer;
    }

    /**
     * play the given {@link MediaPlayer} instance safely (handles null).
     * Stops the player first to ensure it plays from the beginning.
     * @param player The {@link MediaPlayer} to play.
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
                //rectangle.setFill(Color.TRANSPARENT);
                setRectangleData(boardMatrix[i][j], rectangle);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        // initialize the ghost panel
        ghostRectangles = initializeNextBrickPanel(ghostBrickPanel, GameConfig.BRICK_SIZE);
        // set color for ghost piece
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ghostRectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }

        // nextBrick panel initialization (4x4)
        nextBrickRectangles = initializeNextBrickPanel(nextBrickPanel, 12);
        nextBrickRectangles2 = initializeNextBrickPanel(nextBrickPanel2, 10);
        nextBrickRectangles3 = initializeNextBrickPanel(nextBrickPanel3, 10);
        nextBrickRectangles4 = initializeNextBrickPanel(nextBrickPanel4, 10);
        holdBrickRectangle = initializeNextBrickPanel(holdBrickPanel, 12);

        // Initialize the falling brick panel
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

    /**
     * Renders all "next" brick preview panels.
     * @param nextBricks A {@link List} of {@code int[][]} shapes from the queue.
     */
    private void displayNextBricks(List<int[][]> nextBricks) {
        // Panel 1
        displayNextBrick(nextBricks.get(0), nextBrickRectangles);
        // Panel 2
        displayNextBrick(nextBricks.get(1), nextBrickRectangles2);
        // Panel 3
        displayNextBrick(nextBricks.get(2), nextBrickRectangles3);
        // Panel 4
        displayNextBrick(nextBricks.get(3), nextBrickRectangles4);

    }

    /**
     * Helper method to render a single brick shape onto a specific preview panel.
     * @param nextBrick The {@code int[][]} shape to draw.
     * @param rects The 4x4 {@link Rectangle} array (e.g., {@code nextBrickRectangles}) to draw on.
     */
    private void displayNextBrick(int[][] nextBrick, Rectangle[][] rects) {
        // need to initialize the panel to not overwrite
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rects[i][j].setFill(Color.TRANSPARENT);
            }
        }
        // set the next brick
        for (int i = 0; i < nextBrick.length; i++) {
            for (int j = 0; j < nextBrick[i].length; j++) {
                if (nextBrick[i][j] != 0) setRectangleData(nextBrick[i][j], rects[i][j]);
            }
        }
    }

    /**
     * Renders the "hold" brick preview panel.
     * @param holdingBrick The {@code int[][]} shape of the held brick, or {@code null}.
     */
    private void displayHoldBrick(int[][] holdingBrick) {
        // Clear the panel
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                holdBrickRectangle[i][j].setFill(Color.TRANSPARENT);
            }
        }

        // Set the new held brick shape
        if (holdingBrick != null) {
            for (int i = 0; i < holdingBrick.length; i++) {
                for (int j = 0; j < holdingBrick[i].length; j++) {
                    if (holdingBrick[i][j] != 0) {
                        setRectangleData(holdingBrick[i][j], holdBrickRectangle[i][j]);
                    }
                }
            }
        }
    }

    /**
     * A factory helper method to initialize a 4x4 grid of {@link Rectangle} objects.
     *
     * @param panel The {@link GridPane} to add the rectangles to.
     * @param size  The pixel size (width/height) of each rectangle.
     * @return The 2D array of created {@link Rectangle} objects.
     */
    // To efficiently initialize brickpanel with size
    private Rectangle[][] initializeNextBrickPanel(GridPane panel, double size) {
        Rectangle[][] rectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(size, size);
                rectangles[i][j] = rectangle;
                panel.add(rectangle, j, i);
            }
        }
        return rectangles;
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
            case 8 -> Color.GRAY;   // For obstacles
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
           // refresh the dropping brick
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * GameConfig.BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * GameConfig.BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }

            // update ghost piece
            int[][] ghostBrick = brick.getBrickData();

            // X coordinate is the same as dropping brick
            ghostBrickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * GameConfig.BRICK_SIZE);
            // Y coordinate -> get from ViewData
            ghostBrickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getGhostYPosition() * brickPanel.getHgap() + brick.getGhostYPosition() * GameConfig.BRICK_SIZE);

            // Update the ghost brick shape
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    // if there is a block in the 4x4 panel -> pass the brick color
                    if (i < ghostBrick.length && j < ghostBrick[i].length && ghostBrick[i][j] != 0) setRectangleData(ghostBrick[i][j], ghostRectangles[i][j]);
                    // set as transparent
                    else { setRectangleData(0, ghostRectangles[i][j]); }
                }
            }

            // call holding brick and next brick display method
            displayHoldBrick(brick.getHoldBrickData());
            displayNextBricks(brick.getNextBrickData());
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

    /**
     * Handles the result of a "Hard Drop" (user pressing Space).
     * This method is called by the {@link InputHandler} and delegates logic
     * to the controller, then processes the result.
     */
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
