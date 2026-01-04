package com.artkuznet.converter.maxed2.entity.fsm;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private String message = "";

    private String functionName = "";

    private String targetObjectName = "";

    private List<String> params = new ArrayList<>();

    private int unk1 = 0;

    private List<Integer> unk3 = new ArrayList<>();

    public Message() {

    }

    public Message(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public void setTargetObjectName(String targetObjectName) {
        this.targetObjectName = targetObjectName;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public void setUnk1(int unk1) {
        this.unk1 = unk1;
    }

    public String getMessage() {
        return message;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getTargetObjectName() {
        return targetObjectName;
    }

    public List<String> getParams() {
        return params;
    }

    public int getUnk1() {
        return unk1;
    }

    public List<Integer> getUnk3() {
        return unk3;
    }

    public void setUnk3(List<Integer> unk3) {
        this.unk3 = unk3;
    }
}
