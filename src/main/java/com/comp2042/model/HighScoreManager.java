package com.comp2042.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Manages reading and writing the high score to a text file.
 * This adheres to SRP by separating file I/O logic from the GameController.
 */
public class HighScoreManager {
    // file name to save
    private static final String HIGH_SCORE_FILE = "highscore.txt";
    private int highScore;

    public HighScoreManager() { this.highScore = loadHighScore(); }

    /**
     * Loads the high score from the file.
     * @return The saved high score, or 0 if no file exists.
     */
    public int loadHighScore() {
        try {
            File file = new File(HIGH_SCORE_FILE);
            if (!file.exists()) { return 0; }
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextInt()) {
                int score = scanner.nextInt();
                scanner.close();
                return score;
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Failed to load high scsore" + e.getMessage());
        }
        return 0;
    }

    /**
     * Checks if the new score is a high score and saves it to the file.
     * @param newScore The final score from the game.
     * @return true if this was a new high score, false otherwise.
     */
    public boolean saveHighScore(int newScore) {
        if (newScore > this.highScore) {
            this.highScore = newScore;
            // write to the file
            try {
                FileWriter writer = new FileWriter(HIGH_SCORE_FILE, false); // Overwrite
                writer.write(String.valueOf(newScore));
                writer.close();
                return true;
            } catch (IOException e) {
                System.err.println("Falied to save high score" + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Gets the currently loaded high score. (Getter)
     * @return The high score.
     */
    public int getHighScore() {
        return this.highScore;
    }
}