package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.ldb2.jumppoint.JumpPoint;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.util.EntityNameBuilder;

public class JumpPointConverter {

    public static com.artkuznet.converter.maxed2.entity.point.JumpPoint convert(JumpPoint ldbjumpPoint) {

        com.artkuznet.converter.maxed2.entity.point.JumpPoint jumpPoint = new com.artkuznet.converter.maxed2.entity.point.JumpPoint();
//        jumpPoint.setName(ldbjumpPoint.getName().substring(2 + ldbjumpPoint.getName().lastIndexOf("::")));
        jumpPoint.setName(EntityNameBuilder.buildName(ldbjumpPoint.getName()));
        jumpPoint.setLocalMatrix(MatrixUtil.matrixFloatToDouble(ldbjumpPoint.getLocalTransform()));

        return jumpPoint;
    }
}
