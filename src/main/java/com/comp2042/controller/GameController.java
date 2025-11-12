package com.comp2042.controller;

import com.comp2042.GameConfig;
import com.comp2042.model.*;
import com.comp2042.view.GuiController;
import com.comp2042.view.InputEventListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class GameController implements InputEventListener {

    /** The logical game board model. */
    // Original constructor for SimpleBoard -> SimpleBoard(int width, int height) -> ERROR
    private Board board = new SimpleBoard(GameConfig.BOARD_HEIGHT, GameConfig.BOARD_WIDTH);

    /** The main game loop timeline. */
    private Timeline timeLine;

    /** Reference to the GUI controller for updating the view. */
    private final GuiController viewGuiController;
    // Get Difficulty
    private Difficulty difficulty;

    // current game speed with lines
    private double currentGameSpeed;

    // MediaPlayer field
    private MediaPlayer clearRowSoundPlayer;
    private MediaPlayer speedUpSoundPlayer;

    // HighScoreManager reference
    private HighScoreManager highScoreManager;

    /**
     * @param c the {@link GuiController} instance controlling the UI.
     * @param difficulty The selected difficulty (Easy, Normal, Hard)
     */
    public GameController(GuiController c, Difficulty difficulty) {
        viewGuiController = c;
        this.difficulty = difficulty;

        // initialize with difficulty
        initializeDifficulty(difficulty);

        // initialize sound
        initializeSounds();

        // initialize HighScoreManager and pass to GUI
        this.highScoreManager = new HighScoreManager();
        viewGuiController.updateHighScore(highScoreManager.getHighScore()); // Display high score to the GUI

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

        gameLoop();
    }

    /**
     * Saves the current game score, updating the high score if necessary.
     */
    public void saveGameScore() {
        int finalScore = board.getScore().getScore();
        boolean newHighScore = highScoreManager.saveHighScore(finalScore);

        if (newHighScore) {
            viewGuiController.updateHighScore(highScoreManager.getHighScore());
        }
    }

    /**
     * Sets game parameters based on the selected difficulty.
     * @param difficulty The selected difficulty.
     */
    private void initializeDifficulty(Difficulty difficulty) {
        this.currentGameSpeed = GameConfig.GAME_SPEED_MS;
        switch (difficulty) {
            case EASY:
                // speed = 400ms, no change
                break;
            case NORMAL:
                // speed up with clear lines
                //this.currentGameSpeed =
                break;
            case HARD:
                // Normal + obstacle
                break;
        }
    }

    /**
     * Loads sound files into MediaPlayer objects and passes them to the GuiController.
     */
    private void initializeSounds() {
        try {
            URL clearSoundURL = getClass().getResource("/sounds/clearRowSound.mp3");
            URL speedUpSoundURL = getClass().getResource("/sounds/speedUpSound.mp3");

            Media clearMedia = new Media(clearSoundURL.toExternalForm());
            this.clearRowSoundPlayer = new MediaPlayer(clearMedia);
            Media speedUpMedia = new Media(speedUpSoundURL.toExternalForm());
            this.speedUpSoundPlayer = new MediaPlayer(speedUpMedia);
        } catch (Exception e) {
            System.err.println("Failed to load sounds : " + e.getMessage());
        }
        viewGuiController.setupSoundPlayers(this.clearRowSoundPlayer, this.speedUpSoundPlayer);
    }

    /**
     * Initializes and starts the main game loop.
     * The game loop uses a {@link Timeline} that automatically triggers
     * a downward move every 400 milliseconds.
     */
    private void gameLoop() {
        if (timeLine != null) timeLine.stop();

        // set speed (constant for now)
        //double gameSpeed = GameConfig.GAME_SPEED_MS;
        //if (this.difficulty == Difficulty.NORMAL || this.difficulty == Difficulty.HARD) gameSpeed = 300;

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(this.currentGameSpeed),
                ae -> {
                    //onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                    DownData downData = onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                    // Update screen after a down event
                    viewGuiController.refreshBrick(downData.getViewData());
                }
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Handles downward movement events for the current brick.
     * If the brick cannot move down further, it merges into the background,
     * rows are cleared, and a new brick is created.
     *
     * @param event the move event triggering this action.
     * @return the {@link DownData} including updated view and cleared row information.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        // level-up flag
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {   // When row cleared
                board.getScore().add(clearRow.getScoreBonus());
                board.getScore().addToTotalLines(clearRow.getLinesRemoved());
                checkSpeedUp(); // check for speed up
            }
            if (board.createNewBrick()) {
                timeLine.stop();
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handles Hard Drop (move down instantly).
     *
     * @return DownData containing the final board state and score.
     */
    @Override
    public DownData onHardDropEvent() {
        int moved_count = board.hardDrop();
        board.getScore().add(moved_count * 2);
        // same logic as onDownEvent
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            board.getScore().addToTotalLines(clearRow.getLinesRemoved());
            checkSpeedUp(); // check for speed up
        }
        if (board.createNewBrick()) {
            timeLine.stop();
            viewGuiController.gameOver();
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Checks if the line clear count has reached the next level threshold
     * and increases the game speed if necessary.
     */
    private void checkSpeedUp() {
        // no speed change in EASY mode
        if (this.difficulty == Difficulty.EASY) { return; }
        int totalLines = board.getScore().getTotalLinesCleared();
        if (totalLines % 5 == 0) {
            // speed up -> 90% of original
            double newSpeed = this.currentGameSpeed * 0.9;
            if (newSpeed != this.currentGameSpeed) {
                this.currentGameSpeed = newSpeed;
                viewGuiController.playSound(speedUpSoundPlayer);
                viewGuiController.showSpeedUpNotification();
                gameLoop();
            }
        }
    }

    /**
     * Handles movement of the current brick to the left.
     *
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onLeftMostEvnet() {
        board.moveBrickLeftMost();
        return board.getViewData();
    }

    /**
     * Handles movement of the current brick to the right.
     *
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRightMostEvent() {
        board.moveBrickRightMost();
        return board.getViewData();
    }

    /**
     * Handles rotation of the current brick.
     *
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateRightEvent() {
        board.rotateRightBrick();
        return board.getViewData();
    }

    /**
     * Stops the main game loop.
     */
    @Override
    public void stopGame() { timeLine.stop(); }

    /**
     * Resumes the main game loop.
     */
    @Override
    public void resumeGame() { timeLine.play(); }

    /**
     * Starts a new game by resetting the board and refreshing the view.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        timeLine.play();
    }
}
