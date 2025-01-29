package com.artkuznet.converter.maxed;

public class Bitmap {

    private final String name;
    private final int type;
    private final byte[] data;

    public Bitmap(final String name, final int type, final byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public String getExtension() {
        switch (type) {
            case 0:
                return ".tga";
            case 3:
                return ".pcx";
            case 4:
                return ".jpg";
            default:
                return "";
        }
    }
}
