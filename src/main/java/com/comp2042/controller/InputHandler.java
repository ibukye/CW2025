package com.comp2042.controller;

import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;
import com.comp2042.view.GuiController;
import com.comp2042.view.InputEventListener;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * Handles all keyboard input for the game.
 * This class implements EventHandler and interprets KeyEvents,
 * delegating actions to the GuiController and InputEventListener.
 * This separation adheres to the Single Responsibility Principle.
 */
public class InputHandler implements EventHandler<KeyEvent> {
    // Reference to the GuiController
    private final GuiController guiController;
    private final InputEventListener gameController;

    /**
     * Creates a new InputHandler.
     * @param controller The GuiController instance to interact with.
     */
    public InputHandler(GuiController controller, InputEventListener gameController) {
        this.guiController = controller;
        this.gameController = gameController;
    }

    /**
     * Handles the keyboard input (KeyPressed event).
     * Interprets the key code and delegates the appropriate action.
     * @param keyEvent The KeyEvent triggered by the user.
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        if (guiController.isPause() == Boolean.FALSE && guiController.isGameOver() == Boolean.FALSE) {
            if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                guiController.refreshBrick(guiController.getEventListener().onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                keyEvent.consume();
            }
            if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                guiController.refreshBrick(guiController.getEventListener().onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                keyEvent.consume();
            }
            if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                guiController.refreshBrick(guiController.getEventListener().onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                keyEvent.consume();
            }
            if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                guiController.moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                keyEvent.consume();
            }
            if (keyEvent.getCode() == KeyCode.SPACE) {
                ViewData viewData = gameController.onHardDropEvent();
                keyEvent.consume();
            }
        }
        if (keyEvent.getCode() == KeyCode.N) {
            guiController.newGame(null);
        }
    }
}
