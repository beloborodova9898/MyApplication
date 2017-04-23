package com.example.vava.myapplication.Algorithms;

public class Glass {
    private final int maxValue;
    private int currentValue;

    public Glass(int max, int curr) {
        maxValue = max;
        currentValue = curr;
    }

    public int getMaxValue(){
        return maxValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public boolean isEmpty() {
        return (currentValue ==0);
    }

    public boolean isFull() {
        return (currentValue == maxValue);
    }

    public int getFreeVolume() {
        return (maxValue - currentValue);
    }

    public void fill (int toFill) {
        if ((currentValue + toFill) > maxValue) throw new AssertionError("Impossible to add so much liquid.");
        currentValue += toFill;
    }

    public void take (int toTake) {
        if ((currentValue - toTake) < 0) throw new AssertionError("Impossible to take so much liquid.");
        currentValue -= toTake;
    }

    public void makeEmpty() {
        currentValue = 0;
    }

    public void makeFull() {
        currentValue = maxValue;
    }

    public void setCurrentValue(int v) {
        if ((v < 0) || (v > maxValue)) throw new AssertionError("Incorrect value");
        currentValue = v;
    }
}
