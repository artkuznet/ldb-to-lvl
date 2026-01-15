package com.artkuznet.converter.ldb2.jumppoint;

import java.util.ArrayList;
import java.util.List;

public class JumpPointContainer {
    private List<JumpPoint> jumpPoints;

    public JumpPointContainer() {
        this.jumpPoints = new ArrayList<>();
    }

    public JumpPoint get(int key) {
        return jumpPoints.get(key);
    }

    public void set(int key, JumpPoint value) {
        jumpPoints.set(key, value);
    }

    public int size() {
        return jumpPoints.size();
    }

    public void add(JumpPoint jumpPoint) {
        jumpPoints.add(jumpPoint);
    }

    public List<JumpPoint> getList() {
        return jumpPoints;
    }
}
