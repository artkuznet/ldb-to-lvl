package com.artkuznet.converter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Writer {

    private final List<Byte> data = new ArrayList<>();

    private final String fileName;

    public Writer(final String fileName) {
        this.fileName = fileName;
    }

    public void writeByte(final byte b) {
        data.add(b);
    }

    public void writeBytes(final byte[] b) {
        for (final byte value : b) {
            data.add(value);
        }
    }

    public void writeString(final String value) {
        writeBytes(value.getBytes());
    }

    public void writeShort(final short value) {
        data.add((byte) value);
        data.add((byte) (value >> 8));
    }

    public void writeInt(final int value) {
        data.add((byte) value);
        data.add((byte) (value >> 8));
        data.add((byte) (value >> 16));
        data.add((byte) (value >> 24));
    }

    public void writeDouble(double value) {
        long lng = Double.doubleToLongBits(value);
        for (int i = 7; i >= 0; i--) {
            data.add((byte) ((lng >> ((7 - i) * 8)) & 0xff));
        }
    }

    public void save() throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
        final FileOutputStream stream = new FileOutputStream(fileName);
        final byte[] bytes = new byte[data.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = data.get(i);
        }
        stream.write(bytes);
        stream.close();
    }
}
