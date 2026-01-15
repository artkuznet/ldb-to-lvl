package com.artkuznet.converter.util;

import java.nio.charset.StandardCharsets;

public class ChecksumGenerator {
    private static final long CRC64_POLY = 0x42F0E1EBA9EA3693L;
    private static final long[] CRC64_TABLE = new long[256];

    static {
        for (int b = 0; b < 256; b++) {
            long crc = b;
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) == 1) {
                    crc = (crc >>> 1) ^ CRC64_POLY;
                } else {
                    crc >>>= 1;
                }
            }
            CRC64_TABLE[b] = crc;
        }
    }

    public static long generateChecksum(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        long crc = 0xFFFFFFFFFFFFFFFFL;

        for (byte b : bytes) {
            int index = (int) ((crc ^ (b & 0xFF)) & 0xFF);
            crc = (crc >>> 8) ^ CRC64_TABLE[index];
        }

        return -~crc;
    }
}
