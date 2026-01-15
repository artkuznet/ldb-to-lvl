package com.artkuznet.converter.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ResourceUtils {

    private static final int BUFFER_SIZE = 8192; // 8KB buffer

    private ResourceUtils() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static byte[] readDDS(String name) throws IOException {
        try (InputStream is = getResourceAsStream(String.format("com/artkuznet/converter/resources/%s.dds", name))) {
            return readFully(is);
        }
    }

    private static InputStream getResourceAsStream(String path) throws IOException {
        InputStream inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + path);
        }
        return inputStream;
    }

    private static byte[] readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = inputStream.read(chunk, 0, BUFFER_SIZE)) != -1) {
            buffer.write(chunk, 0, bytesRead);
        }

        return buffer.toByteArray();
    }
}
