package com.artkuznet.converter;

import com.artkuznet.converter.ldb.vertex.Vertex;
import com.artkuznet.converter.ldb.vertex.VertexUV;
import com.artkuznet.converter.lv2.Block;

import java.util.List;

public class MaxTypeWriter extends Writer {
    public MaxTypeWriter(String fileName) {
        super(fileName);
    }

    public MaxTypeWriter() {
        super(null);
    }

    public int getSize() {
        return data.size();
    }

    public void write(Object object) {
        if (object instanceof String) {
            writeByte((byte) 0x0D);
            writeStringType((String) object);
        } else if (object instanceof Byte) {
            writeByte((byte) 0x14);
            writeByte((Byte) object);
        } else if (object instanceof Boolean) {
            writeByte((byte) 0x0E);
            writeByte((byte) (((boolean) object) ? 1 : 0));
        } else if (object instanceof Integer) {
            writeByte((byte) 0x00);
            writeInt((Integer) object);
        } else if (object instanceof Short) {
            writeByte((byte) 0x13);
            writeShort((Short) object);
        } else if (object instanceof UByte) {
            writeBytes(new byte[]{0x11, ((UByte) object).getValue()});
        } else if (object instanceof UInt) {
            writeByte((byte) 0x01);
            writeBytes(((UInt) object).toBytes());
        } else if (object instanceof UShort) {
            writeByte((byte) 0x10);
            writeBytes(((UShort) object).toBytes());
        } else if (object instanceof Int3) {
            writeByte((byte) 0x12);
            writeBytes(((Int3) object).toBytes());
        } else if (object instanceof Int2) {
            writeBytes(((Int2) object).toBytes());
        } else if (object instanceof UNumber) {
            writeBytes(((UNumber) object).toBytes());
        } else if (object instanceof Number) {
            writeBytes(((Number) object).toBytes());
        } else if (object instanceof Float) {
            writeByte((byte) 0x09);
            writeFloat((Float) object);
        } else if (object instanceof Double) {
            writeByte((byte) 0x0A);
            writeDouble((Double) object);
        } else if (object instanceof byte[]) {
            writeBytes((byte[]) object);
        } else if (object instanceof Block) {
            writeBlock((Block) object);
        } else if (object instanceof Vector3D) {
            write(((Vector3D) object).getX());
            write(((Vector3D) object).getY());
            write(((Vector3D) object).getZ());
        } else if (object instanceof Vertex) {
            writeByte((byte) 0x16);
            writeFloat(((Vertex) object).getX());
            writeFloat(((Vertex) object).getY());
            writeFloat(((Vertex) object).getZ());
        } else if (object instanceof VertexUV) {
            writeByte((byte) 0x15);
            writeFloat(((VertexUV) object).getU());
            writeFloat(((VertexUV) object).getV());
        } else if (object instanceof List) {
            for (Object o : (List) object) {
                write(o);
            }
        } else if (object == null) {
            throw new RuntimeException("Object is null");
        } else {
            throw new RuntimeException("Unknown object type");
        }
    }

    private void writeStringType(String str) {
        if (str.length() <= Byte.MAX_VALUE) {
            writeByte((byte) 0x14);
            writeByte((byte) str.length());

        } else if (str.length() <= Short.MAX_VALUE) {
            writeByte((byte) 0x13);
            writeShort((short) str.length());
        } else {
            throw new RuntimeException("string too long");
        }

        writeString(str);
    }

    private void writeBlock(Block block) {
        writeInt(block.getValue()[0]);
        writeInt(block.getValue()[1]);
    }
}
