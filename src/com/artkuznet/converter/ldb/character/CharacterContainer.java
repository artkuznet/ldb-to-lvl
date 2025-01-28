package com.artkuznet.converter.ldb.character;

import java.util.ArrayList;
import java.util.List;

public class CharacterContainer {
    private List<Character> characters = new ArrayList<>();

    public void add(Character character) {
        characters.add(character);
    }

    public List<Character> getList() {
        return characters;
    }

    public Character findByName(String name) {
        return characters.stream().filter(c -> c.getSharedName().equals(name)).findFirst().orElse(null);
    }
}
