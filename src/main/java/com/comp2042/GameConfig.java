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

    // Window
    public static final int WINDOW_WIDTH = 400;
    public static final int WINDOW_HEIGHT = 600;

    // Board
    public static final int BOARD_HEIGHT = 25;
    public static final int BOARD_WIDTH = 10;
    public static final int BRICK_SPAWN_X = 4;
    public static final int BRICK_SPAWN_Y = 0;
    public static final int BRICK_SIZE = 20;

    // Hard mode
    public static final double OBSTACLE_PROBABILITY = 0.6;


    // Game timing
    public static final int GAME_SPEED_MS = 400;
    public static final double SPEED_INCREASE_FACTOR = 0.95;
    public static final int ROWS_PER_LEVEL = 5;


    // UI
    public static final int BRICK_PANEL_Y_OFFSET = -42;
    public static final double NEXT_BRICK_SIZE_LARGE = 12.0;
    public static final double NEXT_BRICK_SIZE_SMALL = 10.0;
    public static final double SPEEDUP_NOTIFICATION_Y_OFFSET = 30.0;


    // Score
    public static final int SCORE_BASE_PER_LINE = 50;
    public static final int SOFT_DROP_SCORE = 1;
    public static final int HARD_DROP_SCORE_MULTIPLIER = 2;
}
