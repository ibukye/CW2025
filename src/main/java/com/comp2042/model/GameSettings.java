package com.comp2042.model;

import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class GameSettings {
    // file to save settings
    private static final String SETTINGS_FILE = "settings.txt";
    private String keyBindingMode = "CUSTOM";
    private Properties properties = new Properties();

    public GameSettings() {
        loadSettings();
    }



    public void loadSettings() {
        File file = new File(SETTINGS_FILE);
        if (!file.exists()) {
            setDefaultSettings();
            saveSettings(); // create a new file
            return;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
        }
    }



    /**
     * Sets the default keybindings (your "Custom" layout).
     */
    private void setDefaultSettings() {
        properties.setProperty("MOVE_LEFT", KeyCode.F.name());
        properties.setProperty("MOVE_RIGHT", KeyCode.J.name());
        properties.setProperty("ROTATE_LEFT", KeyCode.S.name());
        properties.setProperty("ROTATE_RIGHT", KeyCode.L.name());
        properties.setProperty("SOFT_DROP", KeyCode.SPACE.name()); // Single Tap
        properties.setProperty("HARD_DROP", KeyCode.SPACE.name()); // Double Tap
        properties.setProperty("MOVE_LEFT_MOST", KeyCode.SLASH.name()); //
        properties.setProperty("MOVE_RIGHT_MOST", KeyCode.SHIFT.name());
        properties.setProperty("HOLD", KeyCode.V.name());
    }



    // save the current setting
    public void saveSettings() {
        try {
            FileWriter writer = new FileWriter(SETTINGS_FILE, false);
            writer.write(this.keyBindingMode);
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to save settings; "+ e.getMessage());
        }
    }

    /**
     * Gets a specific keybinding as a KeyCode.
     * @param action The action (e.g., "MOVE_LEFT").
     * @return The saved KeyCode.
     */
    public KeyCode getKeyBindingMode(String action) {
        String keyName = properties.getProperty(action);
        try {
            return KeyCode.valueOf(keyName);
        } catch (Exception e) {
            System.err.println("Invalid key " + keyName + " for " + action + ", resetting.");
            setDefaultSettings();
            return getKeyCode(action);
        }
    }

    public KeyCode getKeyCode(String action) {
        String keyName = properties.getProperty(action);
        try { return KeyCode.valueOf(keyName); }
        catch (Exception e) {System.err.println("Invalid key"); }
    }

    /**
     * Sets a new keybinding.
     * @param action The action (e.g., "MOVE_LEFT").
     * @param code The new KeyCode.
     */
    public void setKeyBindingMode(String action, KeyCode code) {
        properties.setProperty(action, code.name());
    }




}
