package com.artkuznet.converter.ldb.fsm;

import java.util.ArrayList;
import java.util.List;

public class FSMMessageContainer {
    private List<String> messages = new ArrayList<>();

    public void add(String message) {
        messages.add(message);
    }

    public List<String> getList() {
        return messages;
    }
}
