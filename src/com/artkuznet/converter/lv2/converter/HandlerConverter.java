package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.ldb2.dynamicmesh.DynamicMeshAnimation;
import com.artkuznet.converter.ldb2.fsm.FSMCode2;
import com.artkuznet.converter.maxed2.entity.fsm.Handler;
import com.artkuznet.converter.maxed2.entity.fsm.Message;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerConverter {

    public static Handler convert(String name, FSMCode2 fsmCode, List<String> states) {

        Handler handler = new Handler(name);

        handler.setBeforeMessages(toMessages(fsmCode.getOnBefore()));
        handler.setAfterMessages(toMessages(fsmCode.getOnAfter()));

        LinkedHashMap<String, List<Message>> stateMessages = new LinkedHashMap<>();

        fsmCode.getStates().forEach(
                fsmState -> stateMessages.put(
                        states.get(fsmState.getId()),
                        toMessages(fsmState.getMessages())
                )
        );

        handler.setStateMessages(stateMessages);

        return handler;
    }

    public static Handler convert(DynamicMeshAnimation animation) {
       Handler handler = new Handler( String.format("Animation(%s)", animation.getName()));

        LinkedHashMap<String, List<Message>> stateMessages = new LinkedHashMap<>();

        stateMessages.put("Leave1st", toMessages(animation.getLeavingFirstFrameMessages()));
        stateMessages.put("Enter1st", toMessages(animation.getReturningToFirstFrameMessages()));
        stateMessages.put("Enter2nd", toMessages(animation.getReachingSecondFrameMessages()));

        handler.setStateMessages(stateMessages);

       return handler;
    }

    private static List<Message> toMessages(List<String> messages) {
        return messages.stream()
                .map(Message::new)
                .collect(Collectors.toList());
    }
}
