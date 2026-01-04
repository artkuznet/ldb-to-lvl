package com.artkuznet.converter.lv2.writer;

import com.artkuznet.converter.MaxTypeWriter;
import com.artkuznet.converter.lv2.Block;

import java.util.Collections;
import java.util.List;

public class WriterHelper {
    public static byte[] toBytes(Block block, byte[] data) {
        return toBytes(block, Collections.singletonList(data));
    }

    public static byte[] toBytes(Block block, List<Object> objects) {
        return toBytes(block, objects, true);
    }

    public static byte[] toBytes(Block block, List<Object> objects, boolean offset) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        byte[] data = toBytes(objects);

        int offsetSize = offset ? (8 + 5) : 0;

        if (block == Block.X900 && offset && data.length >= 2) {
            offsetSize -= 2;
        }

        buffer.write(block);
        buffer.write(data.length + offsetSize);
        buffer.writeBytes(data);

        return buffer.toBytes();
    }

    private static byte[] toBytes(List<Object> objects) {
        MaxTypeWriter buffer = new MaxTypeWriter();
        for (Object o : objects) {
            if (o instanceof List) {
                for (Object object : (List) o) {
                    buffer.write(object);
                }
            } else {
                buffer.write(o);
            }
        }
        return buffer.toBytes();
    }
}
