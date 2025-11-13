package com.comp2042.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameSettings {
    // file to save settings
    private static final String SETTINGS_FILE = "settings.txt";
    private String keyBindingMode = "CUSTOM";

    public GameSettings() {
        loadSettings();
    }



    public void loadSettings() {
        try {
            File file = new File(SETTINGS_FILE);
            if (!file.exists()) {
                saveSettings(); // create a new file
                return;
            }
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String mode = scanner.nextLine();
                this.keyBindingMode = mode;
            }
            scanner.close();
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
        }
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

    public String getKeyBindingMode() {
        return this.keyBindingMode;
    }

    public void setKeyBindingMode(String mode) {
        this.keyBindingMode = mode;
    }




}
