package com.artkuznet.converter.ldb2.fsm;

public class FSMTimer {

    private String name;

    private int isRealTime;

    private float length;

    private FSMCode2 startTimer;

    private FSMCode2 endTimer;

    public FSMTimer(String name, int isRealTime, float length, FSMCode2 startTimer, FSMCode2 endTimer) {
        this.name = name;
        this.isRealTime = isRealTime;
        this.length = length;
        this.startTimer = startTimer;
        this.endTimer = endTimer;
    }

    public String getName() {
        return name;
    }

    public int getIsRealTime() {
        return isRealTime;
    }

    public float getLength() {
        return length;
    }

    public FSMCode2 getStartTimer() {
        return startTimer;
    }

    public FSMCode2 getEndTimer() {
        return endTimer;
    }
}
