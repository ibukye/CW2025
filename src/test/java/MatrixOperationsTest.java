import com.comp2042.GameConfig;
import com.comp2042.model.MatrixOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatrixOperationsTest {
    private int[][] emptyBoard;
    private final int[][] brick = new int[][] {
            {0, 1, 0, 0},  // j=0: brick[0][0], brick[0][1], brick[0][2], brick[0][3]
            {0, 1, 0, 0},  // j=1
            {0, 1, 0, 0},  // j=2
            {0, 1, 0, 0}   // j=3
    };



    @BeforeEach
    void setUp() {
        // create an empty board
        emptyBoard = new int[GameConfig.BOARD_HEIGHT][GameConfig.BOARD_WIDTH];
        // fill the down most lines with bricks
        for (int x = 0; x < GameConfig.BOARD_WIDTH; x++) {
            emptyBoard[24][x] = 8;
        }
    }

    @Test
    void testIntersectWithBottomRow() {
        boolean collision = MatrixOperations.intersect(emptyBoard, brick, 3, 23);
        // The brick will collide with bottom row
        assertTrue(collision);
    }

    @Test
    void testIntersectNoCollision() {
        boolean collision = MatrixOperations.intersect(emptyBoard, brick, 3, 0);
        assertFalse(collision);
    }

    @Test
    void testIntersectOutOfBounds() {
        boolean collision = MatrixOperations.intersect(emptyBoard, brick, -3, 0);
        assertTrue(collision);
    }
}

