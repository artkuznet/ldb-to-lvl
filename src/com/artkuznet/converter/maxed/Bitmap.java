package com.artkuznet.converter.maxed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Bitmap {

    private final String name;
    private final int type;
    private final byte[] data;

    public Bitmap(final String name, final int type, final byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public Bitmap(String name) {
        this.name = name;
        try {
            this.data = Files.readAllBytes(Paths.get(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.type = getType(this.data[0]);
    }

    public String getName() {
        return replace8bit(name);
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

    private int getType(byte firstByte) {
        switch (firstByte) {
            case 0x00:
                return 0;
            case 0x0A:
                return 3;
            case (byte) 0xFF:
                return 4;
            default:
                throw new RuntimeException("Unsupported bitmap type");
        }
    }

    // todo util
    private static String replace8bit(String str) {
        return str.chars()
                .mapToObj(c -> (c <= 255) ? String.valueOf((char) c) : "?")
                .collect(Collectors.joining());
    }
}
