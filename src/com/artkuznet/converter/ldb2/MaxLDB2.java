package com.artkuznet.converter.ldb2;

import com.artkuznet.converter.ldb2.character.CharacterContainer;
import com.artkuznet.converter.ldb2.character.CharacterEnemyGroupContainer;
import com.artkuznet.converter.ldb2.dynamiclight.DynamicLightContainer;
import com.artkuznet.converter.ldb2.dynamicmesh.DynamicMeshContainer;
import com.artkuznet.converter.ldb2.flare.FlareContainer;
import com.artkuznet.converter.ldb2.fsm.FSMContainer;
import com.artkuznet.converter.ldb2.jumppoint.JumpPointContainer;
import com.artkuznet.converter.ldb2.levelitem.LevelItemContainer;
import com.artkuznet.converter.ldb2.lightmap.LightMapTextureContainer;
import com.artkuznet.converter.ldb2.material.MaterialContainer;
import com.artkuznet.converter.ldb2.portal.PortalContainer;
import com.artkuznet.converter.ldb2.room.RoomContainer;
import com.artkuznet.converter.ldb2.texture.TextureContainer;
import com.artkuznet.converter.ldb2.trigger.TriggerContainer;
import com.artkuznet.converter.ldb2.waypoint.WayPointContainer;

public class MaxLDB2 {
    private TextureContainer textures;
    private LightMapTextureContainer lightMaps;
    private MaterialContainer materials;
    private RoomContainer rooms;
    private DynamicLightContainer dynamicLights;
    private FlareContainer flares;
    private LevelItemContainer levelItems;
    private PortalContainer portals;
    private JumpPointContainer jumpPoints;
    private WayPointContainer wayPoints;
    private CharacterContainer characters;
    private TriggerContainer triggers;
    private CharacterEnemyGroupContainer characterEnemyGroups;
    private DynamicMeshContainer dynamicMeshes;
    private FSMContainer fsms;

    public MaxLDB2() {
        this.textures = new TextureContainer();
        this.lightMaps = new LightMapTextureContainer();
        this.materials = new MaterialContainer();
        this.rooms = new RoomContainer();
        this.dynamicLights = new DynamicLightContainer();
        this.flares = new FlareContainer();
        this.levelItems = new LevelItemContainer();
        this.portals = new PortalContainer();
        this.jumpPoints = new JumpPointContainer();
        this.wayPoints = new WayPointContainer();
        this.characters = new CharacterContainer();
        this.triggers = new TriggerContainer();
        this.characterEnemyGroups = new CharacterEnemyGroupContainer();
        this.dynamicMeshes = new DynamicMeshContainer();
        this.fsms = new FSMContainer();
    }

    public TextureContainer getTextures() {
        return textures;
    }

    public MaterialContainer getMaterials() {
        return materials;
    }

    public LightMapTextureContainer getLightMaps() {
        return lightMaps;
    }

    public RoomContainer getRooms() {
        return rooms;
    }

    public DynamicLightContainer getDynamicLights() {
        return dynamicLights;
    }

    public FlareContainer getFlares() {
        return flares;
    }

    public LevelItemContainer getLevelItems() {
        return levelItems;
    }

    public JumpPointContainer getJumpPoints() {
        return jumpPoints;
    }

    public WayPointContainer getWayPoints() {
        return wayPoints;
    }

    public PortalContainer getPortals() {
        return portals;
    }

    public CharacterContainer getCharacters() {
        return characters;
    }

    public TriggerContainer getTriggers() {
        return triggers;
    }

    public CharacterEnemyGroupContainer getCharacterEnemyGroups() {
        return characterEnemyGroups;
    }

    public DynamicMeshContainer getDynamicMeshes() {
        return dynamicMeshes;
    }

    public FSMContainer getFSMS() {
        return fsms;
    }
}
