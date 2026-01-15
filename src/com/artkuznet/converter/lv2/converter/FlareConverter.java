package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.ldb2.flare.Flare;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.lv2.converter.helper.MeshCounter;

public class FlareConverter {

    public static com.artkuznet.converter.maxed2.entity.Flare convert(Flare ldbFlare) {

        com.artkuznet.converter.maxed2.entity.Flare flare = new com.artkuznet.converter.maxed2.entity.Flare();
        flare.setLocalMatrix(MatrixUtil.matrixFloatToDouble(ldbFlare.getLocalTransform()));
        flare.setType(ldbFlare.getFlareName());
        flare.setName("Flare_" + MeshCounter.getInstance().next());

        return flare;
    }
}
