package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.EntityLdb2;
import com.artkuznet.converter.ldb2.character.Character;
import com.artkuznet.converter.ldb2.dynamiclight.DynamicLight;
import com.artkuznet.converter.ldb2.flare.Flare;
import com.artkuznet.converter.ldb2.fsm.FSM;
import com.artkuznet.converter.ldb2.jumppoint.JumpPoint;
import com.artkuznet.converter.ldb2.levelitem.LevelItem;
import com.artkuznet.converter.ldb2.waypoint.WayPoint;
import com.artkuznet.converter.maxed2.entity.Entity;

import java.util.Map;

public class EntityConverter {

    public static Entity convert(EntityLdb2 entityLdb2, FSM fsm, Map<Integer, String> characterGroups, Vector3D parentPosition) {

        if (entityLdb2 instanceof Character) {
            return EnemyConverter.convert((Character) entityLdb2, fsm, characterGroups, parentPosition);
        }

        if (entityLdb2 instanceof DynamicLight) {
            return DynamicPointlightConverter.convert((DynamicLight) entityLdb2);
        }

        if (entityLdb2 instanceof Flare) {
            return FlareConverter.convert((Flare) entityLdb2);
        }

        if (entityLdb2 instanceof JumpPoint) {
            return JumpPointConverter.convert((JumpPoint) entityLdb2);
        }

        if (entityLdb2 instanceof LevelItem) {
            return LevelItemConverter.convert((LevelItem) entityLdb2);
        }

        if (entityLdb2 instanceof WayPoint) {
            return WayPointConverter.convert((WayPoint) entityLdb2);
        }

        throw new RuntimeException("unknown type");
    }
}
