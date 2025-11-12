package com.comp2042.model;

import java.util.List;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    //private final int[][] nextBrickData;
    private final List<int[][]> nextBrickData;  // to handle list of bricks
    private final int ghostYPosition;   // Y coordinates of ghost piece

    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBrickData, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostYPosition = ghostYPosition;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int getGhostYPosition() { return ghostYPosition; }

    public List<int[][]> getNextBrickData() {
        //return MatrixOperations.copy(nextBrickData);
        return nextBrickData;
    }
}
