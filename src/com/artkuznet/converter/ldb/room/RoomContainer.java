package com.artkuznet.converter.ldb.room;

import java.util.ArrayList;
import java.util.List;

public class RoomContainer {
    private List<Room> rooms = new ArrayList<>();

    public void add(Room room) {
        rooms.add(room);
    }

    public List<Room> getList() {
        return rooms;
    }
}
