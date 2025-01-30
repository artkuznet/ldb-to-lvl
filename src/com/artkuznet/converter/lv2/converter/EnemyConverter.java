package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.character.Character;
import com.artkuznet.converter.ldb2.fsm.FSM;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.maxed2.entity.Enemy;
import com.artkuznet.converter.maxed2.entity.fsm.FsmData;
import com.artkuznet.converter.maxed2.entity.fsm.Handler;
import com.artkuznet.converter.util.EntityNameBuilder;

import java.util.Arrays;
import java.util.Map;

public class EnemyConverter {

    // TODO refactor

    public static Enemy convert(Character character, FSM fsm, Map<Integer, String> characterGroups, Vector3D parentPosition) {

        if (fsm == null) {
            throw new RuntimeException("character fsm not found");
        }

        if (fsm.getParent() != -1) {
            System.out.println(fsm.getName());
        }

        com.artkuznet.converter.maxed2.entity.Enemy enemy = new com.artkuznet.converter.maxed2.entity.Enemy();

//        enemy.setName(character.getName().substring(2 + character.getName().lastIndexOf("::")));
        enemy.setName(EntityNameBuilder.buildName(character.getName()));
        enemy.setLocalMatrix(MatrixUtil.matrixFloatToDouble(
                character.getLocalTransform()
//                ,
//                fsm.getParent() != -1 ? parentPosition : new Vector3D(0, 0, 0)
//                parentPosition
        ));

        enemy.setType(character.getCharacterName());
        enemy.setActivatorsUseAnimation(character.getActivatorUseAnimation());

        enemy.setGroup(characterGroups.get(character.getEnemyGroupId()));

        enemy.setFsmData(FsmDataConverter.convert(fsm));

        enemy.setLdb2FsmName(fsm.getName());

        return enemy;
    }
}
