package com.artkuznet.converter.ldb;

import com.artkuznet.converter.ldb.bsp.BSPContainer;
import com.artkuznet.converter.ldb.character.CharacterContainer;
import com.artkuznet.converter.ldb.dynamiclight.DynamicLightContainer;
import com.artkuznet.converter.ldb.dynamicmesh.DynamicMeshContainer;
import com.artkuznet.converter.ldb.exit.ExitContainer;
import com.artkuznet.converter.ldb.fsm.FSMContainer;
import com.artkuznet.converter.ldb.item.ItemContainer;
import com.artkuznet.converter.ldb.lightmap.LightmapTextureContainer;
import com.artkuznet.converter.ldb.material.MaterialContainer;
import com.artkuznet.converter.ldb.pointlight.PointLightContainer;
import com.artkuznet.converter.ldb.room.RoomContainer;
import com.artkuznet.converter.ldb.staticmesh.StaticMeshContainer;
import com.artkuznet.converter.ldb.texture.TextureContainer;
import com.artkuznet.converter.ldb.trigger.TriggerContainer;
import com.artkuznet.converter.ldb.waypoint.WaypointContainer;

public class MaxLDB {

    private BSPContainer bsp = new BSPContainer();
    private MaterialContainer materials = new MaterialContainer();
    private TextureContainer textures = new TextureContainer();
    private LightmapTextureContainer lightmaps = new LightmapTextureContainer();
    private ExitContainer exits = new ExitContainer();

    private StaticMeshContainer staticMeshes = new StaticMeshContainer();

    private DynamicLightContainer dynamicLights = new DynamicLightContainer();

    private WaypointContainer waypoints = new WaypointContainer();

    private FSMContainer fsms = new FSMContainer();

    private CharacterContainer characters = new CharacterContainer();

    private TriggerContainer triggers = new TriggerContainer();

    private DynamicMeshContainer dynamicMeshes = new DynamicMeshContainer();

    private ItemContainer items = new ItemContainer();

    private PointLightContainer pointlights = new PointLightContainer();

    private RoomContainer rooms = new RoomContainer();

    public BSPContainer getBsp() {
        return bsp;
    }

    public TextureContainer getTextures() {
        return textures;
    }

    public MaterialContainer getMaterials() {
        return materials;
    }

    public LightmapTextureContainer getLightMaps() {
        return lightmaps;
    }

    public ExitContainer getExits() {
        return exits;
    }

    public StaticMeshContainer getStaticMeshes() {
        return staticMeshes;
    }

    public DynamicLightContainer getDynamicLights() {
        return dynamicLights;
    }

    public WaypointContainer getWaypoints() {
        return waypoints;
    }

    public FSMContainer getFSMs() {
        return fsms;
    }

    public CharacterContainer getCharacters() {
        return characters;
    }

    public TriggerContainer getTriggers() {
        return triggers;
    }

    public DynamicMeshContainer getDynamicMeshes() {
        return dynamicMeshes;
    }

    public ItemContainer getItems() {
        return items;
    }

    public PointLightContainer getPointlights() {
        return pointlights;
    }

    public RoomContainer getRooms() {
        return rooms;
    }
}
