
package com.artkuznet.converter;

public class UShort {

    private final int value;

    public UShort(int value) {
        if (value < 0 || (value & 0xFFFF0000) != 0) {
            throw new RuntimeException();
        }
        this.value = value;
    }

    public byte[] toBytes() {
        return new byte[]{
                (byte) value,
                (byte) (value >> 8)
        };
    }
}
