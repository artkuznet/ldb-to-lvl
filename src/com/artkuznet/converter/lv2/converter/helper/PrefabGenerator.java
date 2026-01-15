package com.artkuznet.converter.lv2.converter.helper;

import com.artkuznet.converter.Vector3D;
import com.artkuznet.converter.ldb2.fsm.FSM;
import com.artkuznet.converter.maxed2.entity.prefab.Prefab;
import com.artkuznet.converter.maxed2.entity.prefab.PrefabParent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrefabGenerator {

    public static List<PrefabParent> generatePrefabs(List<FSM> entities, Vector3D parentPosition) {

        List<PrefabParent> l = entities.stream()
                .filter(e -> e.getName().contains("::prefab::"))
                .filter(e -> {
                    String[] s = e.getName().split("::");
                    return s[s.length - 2].equals("prefab");
                })
                .map(e -> {

                    PrefabParent prefabParent = new PrefabParent();

//                    String name = e.getName().substring(2, e.getName().lastIndexOf("::prefab::"));
//                    name = name.substring(name.indexOf("::") + 2).replace("::", "_");

                    // TODO fix name
                    String name = e.getName().replace("::", "_");
                    name = name.substring(0,name.lastIndexOf(":prefab:"));

                    prefabParent.setName(name);
                    prefabParent.setLocalMatrix(MatrixUtil.matrixFloatToDouble(e.getLocalTransform(), parentPosition));

                    Prefab prefab = new Prefab();
                    prefabParent.addChildEntity(prefab);
                    prefab.setParentEntity(prefabParent);

                    prefabParent.setGameplayCritical(false);

                    return prefabParent;
                })
                .collect(Collectors.toList());

        Map<String, PrefabParent> map = l.stream().collect(Collectors.toMap(PrefabParent::getName, Function.identity(), (a, b) -> a));

        return new ArrayList<>(map.values());
    }
}
