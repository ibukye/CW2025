package com.comp2042.controller;

import com.comp2042.GameConfig;
import com.comp2042.model.*;
import com.comp2042.view.GuiController;
import com.comp2042.view.InputEventListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameController implements InputEventListener {

    /** The logical game board model. */
    // Original constructor for SimpleBoard -> SimpleBoard(int width, int height) -> ERROR
    private Board board = new SimpleBoard(GameConfig.BOARD_HEIGHT, GameConfig.BOARD_WIDTH);

    /** The main game loop timeline. */
    private Timeline timeLine;

    /** Reference to the GUI controller for updating the view. */
    private final GuiController viewGuiController;

    /**
     * Constructor for a new {@code GameController} and initializes the game.
     *
     * @param c the {@link GuiController} instance controlling the UI.
     */
    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

        gameLoop();
    }


    /**
     * Initializes and starts the main game loop.
     * The game loop uses a {@link Timeline} that automatically triggers
     * a downward move every 400 milliseconds.
     */
    private void gameLoop() {
        if (timeLine != null) timeLine.stop();
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(GameConfig.GAME_SPEED_MS),
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
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
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
    public ViewData onHardDropEvent() {
        int moved_count = board.hardDrop();
        board.hardDrop();
        board.getScore().add(moved_count * 2);
        return board.getViewData();
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
