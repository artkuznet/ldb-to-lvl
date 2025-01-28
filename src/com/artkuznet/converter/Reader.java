package com.artkuznet.converter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {

    protected byte[] data;
    protected int offset;

    public byte readByte() {
        return this.data[this.offset++];
    }

    public short readShort() {
        return (short) (this.readByte() & 0xff | this.readByte() << 8 & 0xffff);
    }

    public int readInt() {
        return this.readByte() & 0xff
               | this.readByte() << 8 & 0xffff
               | this.readByte() << 16 & 0xffffff
               | this.readByte() << 24;
    }

    public double readDouble() {
        final byte[] a = this.readBytes(8);
        final byte[] b = new byte[8];
        for (int i = 0, j = 7; i < 8; i++, j--) {
            b[j] = a[i];
        }
        return ByteBuffer.wrap(b).getDouble();
    }

    public float readFloat() {
        final byte[] a = this.readBytes(4);
        final byte[] b = new byte[4];
        for (int i = 0, j = 3; i < 4; i++, j--) {
            b[j] = a[i];
        }
        return ByteBuffer.wrap(b).getFloat();
    }

    public String readString(final int length) {
        return new String(readBytes(length));
    }

    public byte[] readBytes(final int length) {
        final byte[] buffer = new byte[length];
        for (int i = 0; i < length; i++) {
            buffer[i] = readByte();
        }
        return buffer;
    }

    public Reader(final String fileName) {
        try {
            this.data = Files.readAllBytes(
                    Paths.get(fileName)
            );
        } catch (final IOException ignored) {
        }
    }
}
