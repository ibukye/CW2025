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

    // TimeStamp for detecting double space
    private long lastSpacePressTime = 0;
    // Double-tap detection time (ms)
    private static final long DOUBLE_TAP_THRESHOLD = 300;

    /**
     * Creates a new InputHandler.
     * @param controller The {@link GuiController} (View) used for checking game state (isPause, isGameOver)
     * and refreshing the brick display.
     * @param gameController The {@link InputEventListener} (Controller) to which game logic
     * commands (onLeft, onRight, etc.) are sent.
     */
    public InputHandler(GuiController controller, InputEventListener gameController) {
        this.guiController = controller;
        this.gameController = gameController;
    }

    /**
     * Handles the keyboard input (KeyPressed event).
     * Interprets the key code and delegates the appropriate action.
     * Implements a double-tap detection for the SPACE key to differentiate
     * between Soft Drop (single tap) and Hard Drop (double tap).
     * @param keyEvent The KeyEvent triggered by the user.
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        if (guiController.isPause() == Boolean.FALSE && guiController.isGameOver() == Boolean.FALSE) {
            // CAPS : LEFT MOST
            if (keyEvent.getCode() == KeyCode.CAPS) {
                ViewData data = gameController.onLeftMostEvent();
                guiController.refreshBrick(data);
                //guiController.refreshBrick(guiController.getEventListener().onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                keyEvent.consume();
            }
            // F : LEFT
            if (keyEvent.getCode() == KeyCode.F || keyEvent.getCode() == KeyCode.LEFT) {
                guiController.refreshBrick(guiController.getEventListener().onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                keyEvent.consume();
            }
            // J : RIGHT
            if (keyEvent.getCode() == KeyCode.J || keyEvent.getCode() == KeyCode.RIGHT) {
                guiController.refreshBrick(guiController.getEventListener().onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                keyEvent.consume();
            }
            // ENTER : RIGHT MOST
            if (keyEvent.getCode() == KeyCode.ENTER) {
                ViewData data = gameController.onRightMostEvent();
                guiController.refreshBrick(data);
                keyEvent.consume();
            }

            // --- ROTATION ---
            // S : ROTATE LEFT
            if (keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.UP) {
                guiController.refreshBrick(guiController.getEventListener().onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                keyEvent.consume();
            }
            // L : ROTATE RIGHT
            if (keyEvent.getCode() == KeyCode.L) {
                guiController.refreshBrick(guiController.getEventListener().onRotateRightEvent());
                keyEvent.consume();
            }

            // --- DOUBLE SPACE LOGIC ---
            if (keyEvent.getCode() == KeyCode.SPACE) {
                long now = System.currentTimeMillis();

                if (now - lastSpacePressTime < DOUBLE_TAP_THRESHOLD) {
                    // DOUBLE SPACE -> HARD DROP
                    guiController.handleHardDrop();
                    keyEvent.consume();

                    // Reset timer
                    lastSpacePressTime = 0;
                } else {
                    // DOWN
                    guiController.moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    keyEvent.consume();

                    lastSpacePressTime = now;   // record the pressed time
                }
            }

            // Holding brick
            if (keyEvent.getCode() == KeyCode.V) {
                guiController.refreshBrick(guiController.getEventListener().onHoldEvent());
                keyEvent.consume();
            }
        }
        if (keyEvent.getCode() == KeyCode.N) {
            guiController.newGame(null);
        }
    }
}



/*

CAPS : Left most

S : Rotate Left

F : Move left

J : Move right

L : Rotate right

ENTER : Right most


SPACE : Go down
DOUBLE SPACE : Hard drop

 */