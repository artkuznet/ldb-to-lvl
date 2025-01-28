package com.artkuznet.converter.ldb.dynamiclight;

import com.artkuznet.converter.ldb.property.EntityProperties;

public class DynamicLight {
    private String sharedName;
    private EntityProperties objectProperties;
    private float[][] transform;

    private float unk1;
    private float unk2;
    private float unk3;
    private float unk4;
    private float unk5;
    private float unk6;
    private float unk7;
    private float unk8;
    private float unk9;
    private float unk10;

    public DynamicLight(
            String sharedName,
            EntityProperties entityProperties,
            float[][] transform,
            float unk1,
            float unk2,
            float unk3,
            float unk4,
            float unk5,
            float unk6,
            float unk7,
            float unk8,
            float unk9,
            float unk10
    ) {
        this.sharedName = sharedName;
        this.objectProperties = entityProperties;
        this.transform = transform;
        this.unk1 = unk1;
        this.unk2 = unk2;
        this.unk3 = unk3;
        this.unk4 = unk4;
        this.unk5 = unk5;
        this.unk6 = unk6;
        this.unk7 = unk7;
        this.unk8 = unk8;
        this.unk9 = unk9;
        this.unk10 = unk10;
    }
}
