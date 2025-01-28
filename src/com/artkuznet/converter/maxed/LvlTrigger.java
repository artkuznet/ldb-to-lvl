package com.artkuznet.converter.maxed;

public class LvlTrigger extends MaxObject implements PointObject, FSM {

    private String type;

    private double radius;

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public double[] getPosition() {
        return new double[]{0, 0, 0, 0, 0, 0, getRadius()};
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    public static int getObjectType() {
        return 4;
    }

    public void setRadius(final double radius) {
        this.radius = (double) Math.round(radius * 10000d) / 10000d;
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
