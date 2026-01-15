package com.artkuznet.converter.ldb2.character;

import java.util.ArrayList;
import java.util.List;

public class CharacterEnemyGroupContainer {
    private List<CharacterEnemyGroup> enemyGroups;

    public CharacterEnemyGroupContainer() {
        this.enemyGroups = new ArrayList<>();
    }

    public CharacterEnemyGroup get(int key) {
        return enemyGroups.get(key);
    }

    public void set(int key, CharacterEnemyGroup value) {
        enemyGroups.set(key, value);
    }

    public int size() {
        return enemyGroups.size();
    }

    public void add(CharacterEnemyGroup enemyGroup) {
        enemyGroups.add(enemyGroup);
    }

    public List<CharacterEnemyGroup> getList() {
        return enemyGroups;
    }
}
