package com.artkuznet.converter.util;

import com.artkuznet.converter.lv2.converter.helper.MeshCounter;

import java.util.Arrays;

public class EntityNameBuilder {

    public static String buildName(String fullName) {

        String[] objects = fullName.split("::");

        String objectName = objects[objects.length - 1];

        String parentName = objects[objects.length - 2];

        if (objects.length - 2 == 1) {
            return objectName;
        }

        if (parentName.equals("prefab")) {
            if (objects.length - 3 == 1) {
                return objectName;
            }

            parentName = objects[objects.length - 3];
        }

        int prefabIndex = Arrays.asList(objects).indexOf("prefab");
        if (prefabIndex > 0) {
            parentName = objects[prefabIndex - 1] + "_" + parentName;
        }

        return String.format("%s_%s_%s", parentName, objectName, MeshCounter.getInstance().next());
    }
}
