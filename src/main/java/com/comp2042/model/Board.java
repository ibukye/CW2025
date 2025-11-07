package com.comp2042.model;

/**
 * Defines the public contract for any class that represents the core Tetris game board and state.
 * Implementations are responsible for managing the matrix, brick position, and collision logic.
 */
public interface Board {

    /**
     * Attempts to move the current brick down by one unit.
     * @return true if the brick can move, false if collide
     */
    boolean moveBrickDown();

    /**
     * Moves the brick down instantly until it collides.
     * @return The number of rows the brick was dropped.
     */
    int hardDrop();

    /**
     * Attempts to move the current brick left by one unit.
     * @return true if the brick can move, false if collide
     */
    boolean moveBrickLeft();

    /**
     * Attempts to move the current brick right by one unit.
     * @return true if the brick can move, false if collide
     */
    boolean moveBrickRight();

    /**
     * Attempts to rotate the current brick left by 90 degrees.
     * @return true if the brick can move, false if collide
     */
    boolean rotateLeftBrick();

    /**
     * Generates a new random brick, sets it as the currently falling brick,
     * and sets its initial position
     *
     * @return true if the newly spawned brick immediately intersects the background (Game Over condition), false otherwise.
     */
    boolean createNewBrick();

    /**
     * Retrieves the current state of the game board matrix (fixed background bricks).
     *
     * @return The 2D array representing the merged blocks.
     */
    int[][] getBoardMatrix();

    /**
     * Generates and retrieves the current view data for the falling brick and the next brick preview.
     *
     * @return ViewData object containing brick shape, position, and the next brick preview shape.
     */
    ViewData getViewData();

    /**
     * Merges the currently falling brick into the static background matrix (when the brick lands).
     */
    void mergeBrickToBackground();

    /**
     * Checks the current game matrix for complete rows, removes them, and calculates the score bonus.
     * Updates the internal game matrix with the resulting configuration.
     *
     * @return A {@link ClearRow} object detailing the result of the row clearance.
     */
    ClearRow clearRows();

    /**
     * Retrieves the {@link Score} object managing the player's score.
     *
     * @return The current score tracker.
     */
    Score getScore();

    /**
     * Resets the game state: clears the board matrix, resets the score, and spawns a new brick.
     */
    void newGame();
}
