package com.artkuznet.converter.lv2;

public enum Block {
    X100(0xC, 0x100),
    X200(0xC, 0x200),
    X300(0xC, 0x300),
    X400(0xC, 0x400),
    X500(0xC, 0x500),
    X600(0xC, 0x600),
    X900(0xC, 0x900),
    X10C(0x10C, 0);

    Block(int v1, int v2) {
        this.value = new int[]{v1, v2};
    }

    private int[] value;

    public int[] getValue() {
        return value;
    }
}
