package com.artkuznet.converter.ldb.item;

import com.artkuznet.converter.ldb.property.EntityProperties;

public class Item {

    private String sharedName;
    private EntityProperties objectProperties;
    private String itemName;

    public Item(
            String sharedName,
            EntityProperties objectProperties,
            String itemName
    ) {
        this.sharedName = sharedName;
        this.objectProperties = objectProperties;
        this.itemName = itemName;
    }

    public String getSharedName() {
        return sharedName;
    }

    public String getShortName() {
        return sharedName.substring(sharedName.lastIndexOf("::") + 2);
    }

    public String getRoomName() {
        return sharedName.substring(0, sharedName.substring(2).indexOf("::") + 2);
    }

    public EntityProperties getProperties() {
        return objectProperties;
    }

    public String getItemName() {
        return itemName;
    }
}
