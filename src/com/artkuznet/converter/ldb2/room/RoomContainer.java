package com.artkuznet.converter.ldb2.room;

import java.util.ArrayList;
import java.util.List;

public class RoomContainer {
    private List<Room> rooms;

    public RoomContainer() {
        this.rooms = new ArrayList<>();
    }

    public Room get(int key) {
        return rooms.get(key);
    }

    public void set(int key, Room value) {
        rooms.set(key, value);
    }

    public int size() {
        return rooms.size();
    }

    public void add(Room room) {
        rooms.add(room);
    }

    public List<Room> getList() {
        return rooms;
    }
}
