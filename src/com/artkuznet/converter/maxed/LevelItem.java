package com.artkuznet.converter.maxed;

public class LevelItem extends MaxObject implements PointObject {

    private String itemType;

    @Override
    public double[] getPosition() {
        return new double[]{0, 0, 0, 0, 0, 0, getRadius()};
    }

    public static int getObjectType() {
        return 6;
    }

    public void setItemType(final String itemType) {
        this.itemType = itemType;
    }

    @Override
    public String getType() {
        return itemType;
    }
}
