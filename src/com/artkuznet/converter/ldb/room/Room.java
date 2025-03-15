package com.artkuznet.converter.ldb.room;

import java.util.List;

public class Room {
    private int id;
    private String name;
    private List<Integer> staticMeshes;
    private List<String> dynamicLights;
    private List<String> exits;
    private List<String> startPoints;
    private List<String> fsms;
    private List<String> characters;
    private List<String> triggers;
    private List<String> dynamicMeshes;
    private List<String> levelItems;
    private List<Integer> pointLights;
    private float aiNetDensity;

    public Room(
            int id,
            String name,
            List<Integer> staticMeshes,
            List<String> dynamicLights,
            List<String> exits,
            List<String> startPoints,
            List<String> FSMs,
            List<String> characters,
            List<String> triggers,
            List<String> dynamicMeshes,
            List<String> levelItems,
            List<Integer> pointLights,
            float aiNetDensity
    ) {
        this.id = id;
        this.name = name;
        this.staticMeshes = staticMeshes;
        this.dynamicLights = dynamicLights;
        this.exits = exits;
        this.startPoints = startPoints;
        this.fsms = FSMs;
        this.characters = characters;
        this.triggers = triggers;
        this.dynamicMeshes = dynamicMeshes;
        this.levelItems = levelItems;
        this.pointLights = pointLights;
        this.aiNetDensity = aiNetDensity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getStaticMeshes() {
        return staticMeshes;
    }

    public List<String> getDynamicLights() {
        return dynamicLights;
    }

    public List<String> getExits() {
        return exits;
    }

    public List<String> getStartPoints() {
        return startPoints;
    }

    public List<String> getFsms() {
        return fsms;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public List<String> getTriggers() {
        return triggers;
    }

    public List<String> getDynamicMeshes() {
        return dynamicMeshes;
    }

    public List<String> getLevelItems() {
        return levelItems;
    }

    public List<Integer> getPointLights() {
        return pointLights;
    }

    public float getAiNetDensity() {
        return aiNetDensity;
    }
}
