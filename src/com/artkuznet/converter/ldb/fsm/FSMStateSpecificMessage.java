package com.artkuznet.converter.ldb.fsm;

public class FSMStateSpecificMessage {

    private String state_name;

    private FSMMessageContainer messages;

    public FSMStateSpecificMessage(
            String stateName,
            FSMMessageContainer messages
    ) {
        this.state_name = stateName;
        this.messages = messages;
    }

    public FSMMessageContainer getMessages() {
        return messages;
    }

    public String getStateName() {
        return state_name;
    }
}
