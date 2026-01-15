package com.artkuznet.converter.ldb2.fsm;

import java.util.List;

public class FSMState {
    private int id;
    private List<String> messages;

    public FSMState(int id, List<String> messages) {
        this.id = id;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
