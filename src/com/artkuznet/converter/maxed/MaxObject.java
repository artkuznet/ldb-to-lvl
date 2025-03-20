package com.artkuznet.converter.maxed;

import java.util.ArrayList;
import java.util.List;

public abstract class MaxObject implements BaseObject {

    public MaxObject parentObject;

    public String parentName = "";

    public String fullName = "";

    public String newFullName = "";

    public double[] position = new double[7];

    @Override
    public double[] getPosition() {
        return position;
    }

    protected double[][] transform;

    public List<MaxObject> childObjects = new ArrayList<>();

    protected String name;

    protected boolean isHidden;

    @Override
    public double[][] getTransform() {
        if (transform == null) {
            return new double[][]{
                    new double[]{1, 0, 0},
                    new double[]{0, 1, 0},
                    new double[]{0, 0, 1},
                    new double[]{0, 0, 0},
            };
        }

        return transform;
    }

    public void setName(final String name) {
        if (name.length() > Byte.MAX_VALUE) {
            throw new RuntimeException();
        }

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isHidden() {
        return isHidden;
    }

    public void setTransform(final double[][] transform) {
        this.transform = transform;
    }
}
