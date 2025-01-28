package com.artkuznet.converter.ldb.bsp;

import com.artkuznet.converter.ldb.vertex.Vertex;

public class BSPNode {
    private Vertex normal;
    private Vertex pivot;
    private int unk1;
    private int unk2;
    private int unk3;
    private int unk4;
    private int unk5;
    private int unk6;

    public BSPNode(
            Vertex normal,
            Vertex pivot,
            int unk1,
            int unk2,
            int unk3,
            int unk4,
            int unk5,
            int unk6
    ) {
        this.normal = normal;
        this.pivot = pivot;
        this.unk1 = unk1;
        this.unk2 = unk2;
        this.unk3 = unk3;
        this.unk4 = unk4;
        this.unk5 = unk5;
        this.unk6 = unk6;
    }
}
