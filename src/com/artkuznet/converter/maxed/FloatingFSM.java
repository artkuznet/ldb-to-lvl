package com.artkuznet.converter.maxed;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FloatingFSM extends MaxObject implements FSM, Spherical {

    private FSMData fsmData = new FSMData();

    @Override
    public FSMData getFsmData() {
        return this.fsmData;
    }

    @Override
    public void setFsmData(FSMData fsmData) {
        this.fsmData = fsmData;
    }

    public static class FSMData {

        public boolean enemyData = false;

        public List<String> states = new ArrayList<>();

        public String defaultState = "";

        public List<String> customStrings = new ArrayList<>();

        public List<MessageHandler> messageHandlers = new ArrayList<>();

        public static class Message {

            public String message = "";

            public String functionName = "";

            public String targetObjectName = "";

            public List<String> params = new ArrayList<>();
        }

        public static class MessageHandler {

            public String name;

            public List<Message> sendBefore = new ArrayList<>();
            public LinkedHashMap<String, List<Message>> stateSpecific = new LinkedHashMap<>();
            public List<Message> sendAfter = new ArrayList<>();

            public void setName(String name) {
                this.name = name;
            }
        }

        public void setStates(List<String> states) {
            this.states = states;
        }

        public void setDefaultState(String defaultState) {
            this.defaultState = defaultState;
        }

        public void setCustomStrings(List<String> customStrings) {
            this.customStrings = customStrings;
        }

        public void setMessageHandlers(List<MessageHandler> messageHandlers) {
            this.messageHandlers = messageHandlers;
        }
    }

    public static int getObjectType() {
        return 9;
    }

    @Override
    public double[] getPosition() {
        return new double[]{0, 0, 0, 0, 0, 0, getRadius()};
    }
}
