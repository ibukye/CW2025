package com.comp2042;

/**
 * Contains all static configuration values and magic numbers for the game.
 * This class is final and non-instantiable.
 */
public final class GameConfig {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    // No need to create an instance for this just read from this file
    private GameConfig() {}

    // Board dimensions

    /**
     * Height of the game board matrix.
     */
    public static final int BOARD_HEIGHT = 25;
    /**
     * Width of the game board matrix.
     */
    public static final int BOARD_WIDTH = 10;

    /**
     * The initial speed of the game loop's auto-drop in milliseconds.
     */
    // Game timing
    public static final int GAME_SPEED_MS = 400;

    /**
     * The initial X coordinates for spawning new brick
     */
    public static final int BRICK_SPAWN_X = 4;

    /**
     * The initial Y coordinate for spawning new brick
     */
    public static final int BRICK_SPAWN_Y = 10;
}
