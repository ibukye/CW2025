package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    // Calculate the total lines cleared
    private final IntegerProperty totalLinesCleared = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return score;
    }

    // Getter for totalLinesCleared
    public int getTotalLinesCleared() { return totalLinesCleared.get(); }

    // Calculate the total lines cleared
    public void addToTotalLines(int lineCleared) {
        this.totalLinesCleared.setValue(this.totalLinesCleared.get() + lineCleared);
    }

    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    public int getScore() { return score.get(); }

    public void reset() {
        score.setValue(0);
    }
}
