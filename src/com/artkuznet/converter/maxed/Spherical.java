package com.artkuznet.converter.maxed;

public interface Spherical {

    double DEFAULT_RADIUS = 0.5;

    default double getRadius() {
        return DEFAULT_RADIUS;
    }
}
