package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.ldb2.levelitem.LevelItem;
import com.artkuznet.converter.lv2.converter.helper.MatrixUtil;
import com.artkuznet.converter.util.EntityNameBuilder;

public class LevelItemConverter {

    public static com.artkuznet.converter.maxed2.entity.LevelItem convert(LevelItem ldbLevelItem) {

        com.artkuznet.converter.maxed2.entity.LevelItem levelItem = new com.artkuznet.converter.maxed2.entity.LevelItem();

//        levelItem.setName(ldbLevelItem.getName().substring(2 + ldbLevelItem.getName().lastIndexOf("::")));
        levelItem.setName(EntityNameBuilder.buildName(ldbLevelItem.getName()));
        levelItem.setType(ldbLevelItem.getItemName());
        levelItem.setLocalMatrix(MatrixUtil.matrixFloatToDouble(ldbLevelItem.getLocalTransform()));

        return levelItem;
    }
}
