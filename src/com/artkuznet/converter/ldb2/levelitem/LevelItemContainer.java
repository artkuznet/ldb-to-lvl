package com.artkuznet.converter.ldb2.levelitem;

import java.util.ArrayList;
import java.util.List;

public class LevelItemContainer {
    private List<LevelItem> levelItems;

    public LevelItemContainer() {
        this.levelItems = new ArrayList<>();
    }

    public LevelItem get(int index) {
        return levelItems.get(index);
    }

    public void set(int index, LevelItem value) {
        levelItems.set(index, value);
    }

    public int size() {
        return levelItems.size();
    }

    public void add(LevelItem flare) {
        levelItems.add(flare);
    }

    public List<LevelItem> getList() {
        return levelItems;
    }
}