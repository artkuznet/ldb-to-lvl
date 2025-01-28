package com.artkuznet.converter.ldb.fsm;

import java.util.ArrayList;
import java.util.List;

public class FSMStateSpecificMessageContainer {
    private List<FSMStateSpecificMessage> messages = new ArrayList<>();

    public void add(FSMStateSpecificMessage message) {
        messages.add(message);
    }

    public List<FSMStateSpecificMessage> getList() {
        return messages;
    }
}
