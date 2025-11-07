package com.comp2042.view;

import com.comp2042.controller.MoveEvent;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

public interface InputEventListener {

    /**
     * Handles Normal Drop (move down one step).
     * @param event The move event.
     * @return DownData containing the result of the move.
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles movement of the current brick to the left.
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles movement of the current brick to the right.
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles rotation of the current brick.
     *
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles Hard Drop (move down instantly).
     * @return DownData containing the final board state and score.
     */
    DownData onHardDropEvent();

    /**
     * Stops the main game loop.
     */
    void stopGame();

    /**
     * Resumes the main game loop.
     */
    void resumeGame();

    /**
     * Starts a new game by resetting the board and refreshing the view.
     */
    void createNewGame();
}
