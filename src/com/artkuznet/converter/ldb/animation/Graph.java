package com.artkuznet.converter.ldb.animation;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private int sampleRate;
    private List<Float> points = new ArrayList<>();

    public Graph(int sampleRate) {
        if (sampleRate < 0 || sampleRate > 0xFFFF) {
            throw new RuntimeException("wrong sample rate value " + sampleRate);
        }

        this.sampleRate = sampleRate;
    }

    public void addPoint(float point) {
        points.add(point);
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public List<Float> getPoints() {
        return points;
    }
}
