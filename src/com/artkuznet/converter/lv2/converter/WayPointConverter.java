package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.ldb2.waypoint.WayPoint;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.util.EntityNameBuilder;

public class WayPointConverter {

    public static Entity convert(WayPoint ldbWayPoint) {

        Entity point = isAIN(ldbWayPoint)
                ? new com.artkuznet.converter.maxed2.entity.point.AIN()
                : new com.artkuznet.converter.maxed2.entity.point.WayPoint();

//        jumpPoint.setName(ldbjumpPoint.getName().substring(2 + ldbjumpPoint.getName().lastIndexOf("::")));
        point.setName(EntityNameBuilder.buildName(ldbWayPoint.getName()));
        point.setLocalMatrix(MatrixUtil.matrixFloatToDouble(ldbWayPoint.getLocalTransform()));

        return point;
    }

    private static boolean isAIN(WayPoint ldbWayPoint) {
        return ldbWayPoint.getName().substring(ldbWayPoint.getName().lastIndexOf("::")).toLowerCase().contains("ai")
                && !ldbWayPoint.getName().toLowerCase().contains("w");
    }
}
