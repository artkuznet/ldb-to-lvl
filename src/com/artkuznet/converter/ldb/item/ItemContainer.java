package com.artkuznet.converter.ldb.item;

import java.util.ArrayList;
import java.util.List;

public class ItemContainer {
    private List<Item> items = new ArrayList<>();

    public void add(Item item) {
        items.add(item);
    }

    public List<Item> getList() {
        return items;
    }
}
