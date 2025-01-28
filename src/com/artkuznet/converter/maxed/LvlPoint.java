package com.artkuznet.converter.maxed;

public class LvlPoint extends MaxObject implements Spherical {

    private int type;

    public void setType(final int type) {
        this.type = type;
    }

    @Override
    public double[] getPosition() {
        return new double[]{0, 0, 0, 0, 0, 0, getRadius()};
    }

    public int getType() {
        return type;
    }

    public static int getObjectType() {
        return 3;
    }
}
