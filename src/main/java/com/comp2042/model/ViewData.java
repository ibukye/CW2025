package com.comp2042.model;

import java.util.List;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    //private final int[][] nextBrickData;
    private final List<int[][]> nextBrickData;  // to handle list of bricks

    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
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

    public List<int[][]> getNextBrickData() {
        //return MatrixOperations.copy(nextBrickData);
        return nextBrickData;
    }
}
