package com.comp2042.model;

import com.comp2042.GameConfig;
import com.comp2042.model.bricks.Brick;
import com.comp2042.model.bricks.BrickGenerator;
import com.comp2042.model.bricks.RandomBrickGenerator;

import java.awt.*;
import java.util.List;


/**
 * {@code SimpleBoard} is the concrete implementation of the {@link Board} interface.
 * It manages the core state of the Tetris game, including the background matrix of
 * merged bricks, the position of the currently falling brick, and the score.
 * This class adheres to the Single Responsibility Principle by delegating
 * row clearing logic (TBD: to RowClearer) and brick generation/rotation to specialized classes.
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    /**
     * Initializes a new SimpleBoard with the specified dimensions.
     * Sets up the game matrix, brick generators, and the score tracker.
     *
     * @param width The width of the game matrix.
     * @param height The height of the game matrix.
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Attempts to move the currently falling brick down by one unit.
     * Checks for collision against the bottom boundary or fixed background bricks.
     *
     * @return true if the move was successful, false if a collision occurred.
     */
    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Moves the brick down instantly until it collides.
     * @return The number of rows the brick was dropped.
     */
    @Override
    public int hardDrop() {
        int moved_count = 0;
        while (moveBrickDown()) moved_count++;
        return moved_count;
    }

    /**
     * Attempts to move the currently falling brick left by one unit.
     * Checks for collision against the left boundary or fixed background bricks.
     *
     * @return true if the move was successful, false if a collision occurred.
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Moves the brick instantly to the far left boundary or until it hits another brick.
     */
    @Override
    public void moveBrickLeftMost() {
        // go left until collide
        while(moveBrickLeft()) {}
    }

    /**
     * Attempts to move the currently falling brick right by one unit.
     * Checks for collision against the right boundary or fixed background bricks.
     *
     * @return true if the move was successful, false if a collision occurred.
     */
    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Moves the brick instantly to the far right boundary or until it hits another brick.
     */
    @Override
    public void moveBrickRightMost() {
        // move right until collide
        while(moveBrickRight()) {}
    }

    /**
     * Attempts to rotate the currently falling brick 90 degrees left.
     * Checks for collision against boundaries or fixed background bricks after rotation.
     *
     * @return true if the rotation was successful, false if a collision occurred.
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /**
     * Attempts to rotate the currently falling brick 90 degrees right.
     * Checks for collision against boundaries or fixed background bricks after rotation.
     *
     * @return true if the rotation was successful, false if a collision occurred.
     */
    @Override
    public boolean rotateRightBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo prevShape = brickRotator.getPrevShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, prevShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) { return false; }
        else { brickRotator.setCurrentShape(prevShape.getPosition()); return true; }
        //System.out.println("Rotate RIght");
    }

    /**
     * Generates a new random brick, sets it as the currently falling brick,
     * and sets its initial position based on {@code GameConfig}.
     *
     * @return true if the newly spawned brick immediately intersects the background (Game Over condition), false otherwise.
     */
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        // Determines the spawn point of new brick
        currentOffset = new Point(GameConfig.BRICK_SPAWN_X, GameConfig.BRICK_SPAWN_Y);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Retrieves the current state of the game board matrix (fixed background bricks).
     *
     * @return The 2D array representing the merged blocks.
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }


    private int calculateGhostY() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        int[][] shape = brickRotator.getCurrentShape();
        int currentX = (int) currentOffset.getX();
        int currentY = (int) currentOffset.getY();

        // increase the Y coordinates until it collide
        while (!MatrixOperations.intersect(currentMatrix, shape, currentX, currentY+1)) {
            currentY++;
        }
        return currentY;
    }

    /**
     * Generates and retrieves the current view data for the falling brick and the next brick preview.
     *
     * @return ViewData object containing brick shape, position, and the next brick preview shape.
     */
    @Override
    public ViewData getViewData() {
        List<int[][]> nextShape = ((RandomBrickGenerator) brickGenerator).getNextBrickShape();
        int ghostY = calculateGhostY();

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                nextShape,
                ghostY
        );

        //return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
    }

    /**
     * Merges the currently falling brick into the static background matrix (when the brick lands).
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Checks the current game matrix for complete rows, removes them, and calculates the score bonus.
     * Updates the internal game matrix with the resulting configuration.
     *
     * @return A {@link ClearRow} object detailing the result of the row clearance.
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    /**
     * Retrieves the {@link Score} object managing the player's score.
     *
     * @return The current score tracker.
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Resets the game state: clears the board matrix, resets the score, and spawns a new brick.
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }
}
