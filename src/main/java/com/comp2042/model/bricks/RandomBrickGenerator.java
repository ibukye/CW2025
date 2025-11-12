package com.comp2042.model.bricks;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomBrickGenerator implements BrickGenerator {

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    // add next brick to queue
    private Queue<Brick> upcomingBricks;

    // queue size
    private static final int UPCOMING_QUEUE_SIZE = 4;


    /*public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    }*/

    public RandomBrickGenerator() {
        // initialize the queue and fill with 4 bricks
        upcomingBricks = new LinkedList<>();
        for (int i = 0; i < UPCOMING_QUEUE_SIZE; i++) {
            upcomingBricks.add(newBrick());
        }
    }

    @Override
    public Brick getBrick() {
        /*if (nextBricks.size() <= 1) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }
        return nextBricks.poll();*/

        Brick brick = upcomingBricks.poll();
        upcomingBricks.add(newBrick());
        return brick;
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    public List<int[][]> getNextBrickShape() {
        // empty list for storing results
        List<int[][]> shapesList = new ArrayList<>();
        // take the inside of the queue as brick and iterate
        for (Brick brick : upcomingBricks) {
            // get the default shape
            int[][] shape = brick.getShapeMatrix().get(0);
            // append the shape to empty list
            shapesList.add(shape);
        }
        return shapesList;
    }

    private Brick newBrick() {
        int randomBrick = (int) (Math.random() * 7);    // 0 ~ 6

        return switch (randomBrick) {
            case 0 -> new IBrick();
            case 1 -> new JBrick();
            case 2 -> new LBrick();
            case 3 -> new OBrick();
            case 4 -> new SBrick();
            case 5 -> new TBrick();
            case 6 -> new ZBrick();
            default -> new IBrick();
        };
    }
}
