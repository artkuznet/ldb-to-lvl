package com.artkuznet.converter;

public class Int3 {

    private final int value;

    public Int3(int value) {
        this.value = value;
    }

    public byte[] toBytes() {
        return new byte[]{
                (byte) value,
                (byte) (value >> 8),
                (byte) (value >> 16)
        };
    }
}
