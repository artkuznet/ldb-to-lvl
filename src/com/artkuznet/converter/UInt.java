package com.artkuznet.converter;

public class UInt {

    private final int value;

    public UInt(int value) {
        this.value = value;
    }

    public byte[] toBytes() {
        return new byte[]{
                (byte) value,
                (byte) (value >> 8),
                (byte) (value >> 16),
                (byte) (value >> 24),
        };
    }
}
