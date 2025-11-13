package com.comp2042.model;

import com.comp2042.model.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Gets the next rotation shape from the brick's shape list (Rotate Left).
     * This method increments the current shape index and wraps around
     * to the beginning if it reaches the end of the list.
     *
     * @return A {@link NextShapeInfo} object containing the 2D array of the next shape
     * and its corresponding index.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        // index+1 -> take the modulo of the list size
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Gets the previous rotation shape from the brick's shape list (Rotate Right).
     * This method decrements the current shape index and wraps around
     * to the end of the list if it goes below zero.
     *
     * @return A {@link NextShapeInfo} object containing the 2D array of the previous shape
     * and its corresponding index.
     */
    // ROTATE RIGHT
    public NextShapeInfo getPrevShape() {
        int prevShape = currentShape;
        // index-1
        //Exception in thread "JavaFX Application Thread" java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 4
        prevShape = (--prevShape + brick.getShapeMatrix().size()) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(prevShape), prevShape);
    }

    /**
     * Gets the 2D array of the brick's current rotational shape.
     *
     * @return The {@code int[][]} matrix of the current shape.
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public Brick getBrick() { return this.brick; }

    /**
     * Manually sets the current rotation to a specific index.
     * This is used by the Board to confirm a rotation after a successful collision check.
     *
     * @param currentShape The new index to set as the current shape.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the active brick to be managed by the rotator.
     * This resets the current shape index to 0.
     *
     * @param brick The new {@link Brick} to manage.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
