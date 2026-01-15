package com.artkuznet.converter.ldb2.fsm;

import com.artkuznet.converter.ldb2.EntityLdb2;

import java.util.List;
import java.util.Map;

public class FSM implements EntityLdb2 {
    private int index;
    private String name;
    private float[][] transform;
    private int parent;
    private float[][] localTransform;
    private int roomId;
    private List<String> stateNames;
    private String defaultState;
    private FSMCode2 onStartup;
    private Map<String, FSMCode2> events;
    private Map<String, FSMCode2> customEvents;
    private List<FSMTimer> timers;

    public FSM(
            int index,
            String name,
            float[][] transform,
            int parent,
            float[][] localTransform,
            int roomId,
            List<String> stateNames,
            String defaultState,
            FSMCode2 onStartup,
            Map<String, FSMCode2> events,
            Map<String, FSMCode2> customEvents,
            List<FSMTimer> timer
    ) {
        this.index = index;
        this.name = name;
        this.transform = transform;
        this.parent = parent;
        this.localTransform = localTransform;
        this.roomId = roomId;
        this.stateNames = stateNames;
        this.defaultState = defaultState;
        this.onStartup = onStartup;
        this.events = events;
        this.customEvents = customEvents;
        this.timers = timer;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[][] getTransform() {
        return transform;
    }

    public void setTransform(float[][] transform) {
        this.transform = transform;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public float[][] getLocalTransform() {
        return localTransform;
    }

    public void setLocalTransform(float[][] localTransform) {
        this.localTransform = localTransform;
    }

    @Override
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public List<FSMTimer> getTimers() {
        return timers;
    }

    public List<String> getStateNames() {
        return stateNames;
    }

    public String getDefaultState() {
        return defaultState;
    }

    public FSMCode2 getOnStartup() {
        return onStartup;
    }

    public Map<String, FSMCode2> getEvents() {
        return events;
    }

    public Map<String, FSMCode2> getCustomEvents() {
        return customEvents;
    }
}
