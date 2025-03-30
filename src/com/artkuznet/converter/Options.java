package com.artkuznet.converter;

public final class Options {

    private static Options INSTANCE;

    private Options() {
    }

    public boolean skipJoinPolygons = false;

    public static Options getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Options();
        }

        return INSTANCE;
    }
}
