package com.artkuznet.converter;

public class Number {

    private final int value;

    public Number(int value) {
        if (value > 0x7FFFFF || value < -0x800000) {
            throw new RuntimeException();
        }
        this.value = value;
    }

    public byte[] toBytes() {
        return Math.abs(value) <= Byte.MAX_VALUE
                ? new byte[]{0x14, (byte) value}
                : Math.abs(value) <= Short.MAX_VALUE
                ? new byte[]{0x13, (byte) value, (byte) (value >> 8)}
                : new byte[]{0x12, (byte) value, (byte) (value >> 8), (byte) (value >> 16)};
    }
}
