package com.artkuznet.converter.ldb.fsm;

public class FSMEvent {

    private String stateName;
    private FSMMessageContainer before;
    private FSMStateSpecificMessageContainer stateSpecific;
    private FSMMessageContainer after;

    public FSMEvent(
            String stateName,
            FSMMessageContainer before,
            FSMStateSpecificMessageContainer stateSpecific,
            FSMMessageContainer after
    ) {
        this.stateName = stateName;
        this.before = before;
        this.stateSpecific = stateSpecific;
        this.after = after;
    }

    public String getStateName() {
        return stateName;
    }

    public FSMMessageContainer getBefore() {
        return before;
    }

    public FSMStateSpecificMessageContainer getStateSpecific() {
        return stateSpecific;
    }

    public FSMMessageContainer getAfter() {
        return after;
    }
}
