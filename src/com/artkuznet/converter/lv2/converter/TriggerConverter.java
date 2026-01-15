package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.fsm.FSM;
import com.artkuznet.converter.ldb2.trigger.Trigger;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.maxed2.entity.TriggerData;
import com.artkuznet.converter.util.EntityNameBuilder;

import java.util.Arrays;

public class TriggerConverter {

    public static com.artkuznet.converter.maxed2.entity.Trigger convert(Trigger ldbTrigger, FSM fsm, Vector3D parentPosition) {
        com.artkuznet.converter.maxed2.entity.Trigger trigger = new com.artkuznet.converter.maxed2.entity.Trigger();

//        trigger.setName(fsm.getName().substring(2 + fsm.getName().lastIndexOf("::")));
        trigger.setName(EntityNameBuilder.buildName(fsm.getName()));
        trigger.setLocalMatrix(MatrixUtil.matrixFloatToDouble(fsm.getLocalTransform(), parentPosition));
        trigger.setRadius(ldbTrigger.getRadius());

        TriggerData data = new TriggerData();

        data.setActivatorsUseAnimation(ldbTrigger.getActivatorsUseAnimation());

        data.setPlayer(1 == ldbTrigger.getActivationPlayer());
        data.setUse(1 == ldbTrigger.getActivationUse());
        data.setEnemy(1 == ldbTrigger.getActivationEnemy());
        data.setBullet(1 == ldbTrigger.getActivationBullet());
        data.setLookAt(1 == ldbTrigger.getActivationLookAt());
        data.setVisibility(1 == ldbTrigger.getActivationVisibility());

        trigger.setData(data);

        trigger.setFsmData(FsmDataConverter.convert(fsm));

        trigger.setLdb2FsmName(fsm.getName());

        return trigger;
    }
}
