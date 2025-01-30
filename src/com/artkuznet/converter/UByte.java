package com.artkuznet.converter;

public class UByte {

    private final byte value;

    public UByte(int value) {
        if (value < 0 || (value & 0xFFFFFF00) != 0) {
            throw new RuntimeException();
        }
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
