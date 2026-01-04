package com.artkuznet.converter;

public class Int2 {

    private final int value;

    public Int2(int value) {
        this.value = value;
    }

    public byte[] toBytes() {
        return new byte[]{
                0x02,
                (byte) value,
                (byte) (value >> 8),
                (byte) (value >> 16),
                (byte) (value >> 24)
        };
    }
}
