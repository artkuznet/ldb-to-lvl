package com.artkuznet.converter.lv2.converter.helper;

public class MeshCounter {
    private static MeshCounter INSTANCE;

    private MeshCounter() {
    }

    private int counter = -1;

    public int next() {
        return ++counter;
    }

    public static MeshCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MeshCounter();
        }

        return INSTANCE;
    }
}
