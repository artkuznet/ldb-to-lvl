package com.artkuznet.converter.lv2.converter.helper;

public class PolygonIndexCounter {
    private static PolygonIndexCounter INSTANCE;

    private PolygonIndexCounter() {
    }

    private int counter = -1;

    public int next() {
        return ++counter;
    }

    public void reset() {
        counter = -1;
    }

    public static PolygonIndexCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PolygonIndexCounter();
        }

        return INSTANCE;
    }
}
