package com.artkuznet.converter.ldb;

import com.artkuznet.converter.Reader;
import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexUV;

public final class MaxTypeReader extends Reader {

    public MaxTypeReader(String fileName) {
        super(fileName);
    }

    public Object readObject() {
        var type = readByte();

        return switch (type) {
            case 0x00 -> readInt();
            case 0x02 -> readInt();
            case 0x03 -> readUInt(); // unsigned
            case 0x0D -> readObjectString();
            case 0x0E -> (int) readByte(); // bool
            case 0x0F -> readUInt3(); // unsigned
            case 0x09 -> readFloat();
            case 0x10 -> readUShort(); // unsigned
            case 0x11 -> readUByte(); // unsigned
            case 0x12 -> readInt3();
            case 0x13 -> (int) readShort();
            case 0x14 -> (int) readByte();
            case 0x15 -> readVector2D();
            case 0x16 -> readVector3D();
            case 0x19 -> readMatrix3x3();
            case 0x1A -> readMatrix4x3();
            default -> throw new RuntimeException("readObject wrong type " + type);
        };
    }

    private String readObjectString() {
        var type = readByte();

        return switch (type) {
            case 0x11 -> readString(readByte());
            case 0x12 -> readString(readInt3());
            case 0x13 -> readString(readShort());
            case 0x14 -> readString(readByte());
            default -> throw new RuntimeException("readObjectString wrong type " + type);
        };
    }

    private int readInt3() {
        return this.readByte() & 0xff
                | this.readByte() << 8 & 0xffff
                | this.readByte() << 16 & 0xffffff;
    }

    private VertexUV readVector2D() {
        return new VertexUV(readFloat(), readFloat());
    }

    private Vertex readVector3D() {
        return new Vertex(readFloat(), readFloat(), readFloat());
    }

    private Object readMatrix4x3() {
        return new float[][]{
                new float[]{readFloat(), readFloat(), readFloat()},
                new float[]{readFloat(), readFloat(), readFloat()},
                new float[]{readFloat(), readFloat(), readFloat()},
                new float[]{readFloat(), readFloat(), readFloat()},
        };
    }

    private Object readMatrix3x3() {
        return new float[][]{
                new float[]{readFloat(), readFloat(), readFloat()},
                new float[]{readFloat(), readFloat(), readFloat()},
                new float[]{readFloat(), readFloat(), readFloat()},
        };
    }

    private int readUInt() {
        return readInt();
    }

    private int readUInt3() {
        var x = readInt3();

        if (x < 0) {
            throw new RuntimeException("readUInt3 value < 0");
        }

        return x;
    }

    private int readUShort() {
        return readShort() & 0xFFFF;
    }

    private int readUByte() {
        return readByte() & 0xFF;
    }
}
