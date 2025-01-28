package com.artkuznet.converter.maxed;

public class Enemy extends MaxObject implements PointObject, FSM {

    private String type;

    @Override
    public double[] getPosition() {
        return new double[]{0, 0, 0, 0, 0, 0, getRadius()};
    }

    public static int getObjectType() {
        return 5;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    private FloatingFSM.FSMData fsmData = new FloatingFSM.FSMData();

    @Override
    public FloatingFSM.FSMData getFsmData() {
        return this.fsmData;
    }

    @Override
    public void setFsmData(FloatingFSM.FSMData fsmData) {
        this.fsmData = fsmData;
    }
}
