
package com.artkuznet.converter;

public class UNumber {

    private final int value;

    public UNumber(int value) {
        this.value = value;
    }

    public byte[] toBytes() {
        if ((value & 0xFF000000) != 0) {
            return new byte[]{
                    0x03,
                    (byte) value,
                    (byte) (value >> 8),
                    (byte) (value >> 16),
                    (byte) (value >> 24)
            };
        }

        return (value & 0xFFFFFF00) == 0
                ? new byte[]{0x11, (byte) value}
                : (value & 0xFFFF0000) == 0
                ? new byte[]{0x10, (byte) value, (byte) (value >> 8)}
                : new byte[]{0x0F, (byte) value, (byte) (value >> 8), (byte) (value >> 16)};
    }
}
