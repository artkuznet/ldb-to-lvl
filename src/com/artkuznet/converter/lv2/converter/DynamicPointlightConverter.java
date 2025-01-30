package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.ldb2.dynamiclight.DynamicLight;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.lv2.converter.helper.MeshCounter;
import com.artkuznet.converter.maxed2.entity.DynamicPointlight;

public class DynamicPointlightConverter {

    public static DynamicPointlight convert(DynamicLight ldbDynamicLight) {
        DynamicPointlight dynamicPointlight = new DynamicPointlight();

        dynamicPointlight.setName("Dynamic_Pointlight_" + MeshCounter.getInstance().next());

        dynamicPointlight.setLocalMatrix(MatrixUtil.matrixFloatToDouble(ldbDynamicLight.getLocalTransform()));
        dynamicPointlight.setFalloff(ldbDynamicLight.getFalloff());

        dynamicPointlight.setColor(new DynamicPointlight.Color(
                ldbDynamicLight.getColor().getR(),
                ldbDynamicLight.getColor().getG(),
                ldbDynamicLight.getColor().getB(),
                ldbDynamicLight.getColor().getA()
        ));

        return dynamicPointlight;
    }
}
