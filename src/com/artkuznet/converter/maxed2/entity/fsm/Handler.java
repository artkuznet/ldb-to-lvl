package com.artkuznet.converter.maxed2.entity.fsm;

import java.util.*;

public class Handler {

    private String name;

    private List<Message> beforeMessages = new ArrayList<>();

    private LinkedHashMap<String, List<Message>> stateMessages = new LinkedHashMap<>();

    private List<Message> afterMessages = new ArrayList<>();

    public Handler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getBeforeMessages() {
        return beforeMessages;
    }

    public void setBeforeMessages(List<Message> beforeMessages) {
        this.beforeMessages = beforeMessages;
    }

    public Map<String, List<Message>> getStateMessages() {
        return stateMessages;
    }

    public void setStateMessages(LinkedHashMap<String, List<Message>> stateMessages) {
        this.stateMessages = stateMessages;
    }

    public List<Message> getAfterMessages() {
        return afterMessages;
    }

    public void setAfterMessages(List<Message> afterMessages) {
        this.afterMessages = afterMessages;
    }
}
