package com.artkuznet.converter.ldb2.character;

import java.util.ArrayList;
import java.util.List;

public class CharacterContainer {
    private List<Character> characters;

    public CharacterContainer() {
        this.characters = new ArrayList<>();
    }

    public Character get(int key) {
        return characters.get(key);
    }

    public void set(int key, Character value) {
        characters.set(key, value);
    }

    public int size() {
        return characters.size();
    }

    public void add(Character character) {
        characters.add(character);
    }

    public List<Character> getList() {
        return characters;
    }
}
