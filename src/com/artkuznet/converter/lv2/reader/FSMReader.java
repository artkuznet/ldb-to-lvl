package com.artkuznet.converter.lv2.reader;

import com.artkuznet.converter.MaxTypeReader;
import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.fsm.*;
import com.artkuznet.converter.maxed2.entity.fsm.Timer;
import com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh;
import com.artkuznet.converter.maxed2.entity.mesh.DynamicTriangleMesh;
import com.artkuznet.converter.maxed2.entity.prefab.PrefabParent;

import java.util.*;

public class FSMReader {

    public static Entity read(Entity entity, MaxTypeReader reader) {
        if (!(entity instanceof FSM)) {
            throw new RuntimeException();
        }

        int fsmDataLength1 = (int) reader.readObject();
        ReaderHelper.read100(reader);
        int fsmDataLength = (int) reader.readObject();
        if (fsmDataLength != fsmDataLength1) {
            throw new RuntimeException();
        }

        FsmData fsmData = new FsmData();

        if (29 != reader.readByte()) {
            throw new RuntimeException();
        }
        int stateNameCount = (int) reader.readObject(); // 0x14
        List<String> stateNames = new ArrayList<>();
        for (int i = 0; i < stateNameCount; i++) {
            stateNames.add((String) reader.readObject());
        }

        fsmData.setStateNames(stateNames);
        fsmData.setDefaultStateName((String) reader.readObject());

        if (29 != reader.readByte()) {
            throw new RuntimeException();
        }

        int customEventsCount = (int) reader.readObject();
        List<String> customEventNames = new ArrayList<>();
        for (int i = 0; i < customEventsCount; i++) {
            customEventNames.add((String) reader.readObject());
        }

        fsmData.setCustomEventNames(customEventNames);

        if (31 != reader.readByte()) {
            throw new RuntimeException();
        }

        List<Handler> handlers = new ArrayList<>();

        int handlerCount = (int) reader.readObject(); //0x14
        for (int i = 0; i < handlerCount; i++) {
            Handler handler = new Handler((String) reader.readObject());

            handler.setBeforeMessages(readMessages(reader));

            if (31 != reader.readByte()) {
                throw new RuntimeException();
            }

            LinkedHashMap<String, List<Message>> stateMessages = new LinkedHashMap<>();
            int stateMessagesCount = (int) reader.readObject(); // 0x14
            for (int j = 0; j < stateMessagesCount; j++) {
                stateMessages.put((String) reader.readObject(), readMessages(reader));
            }

            handler.setStateMessages(stateMessages);
            handler.setAfterMessages(readMessages(reader));

            handlers.add(handler);
        }

        fsmData.setHandlers(handlers);

        fsmData.setUnk1(1 == (int) reader.readObject()); // 0x0E
        fsmData.setUnk2(1 == (int) reader.readObject()); // 0x0E

        if (31 != reader.readByte()) {
            throw new RuntimeException();
        }

        List<Timer> timers = new ArrayList<>();
        int timerCount = (int) reader.readObject();
        for (int i = 0; i < timerCount; i++) {
            Timer timer = new Timer();

            timer.setName((String) reader.readObject());

            int offset = reader.getOffset();

            ReaderHelper.read100(reader);

            int timerDataSize = (int) reader.readObject(); // 0x00

            timer.setTypeToggle(1 == (int) reader.readObject());
            timer.setLength((float) reader.readObject());

            if (reader.getOffset() - offset != timerDataSize) {
                throw new RuntimeException("Invalid data size");
            }

            timers.add(timer);
        }

        fsmData.setTimers(timers);

        if (entity instanceof DynamicMesh) {
            ReaderHelper.read10C(reader);
        } else if (entity instanceof PrefabParent || entity instanceof DynamicTriangleMesh) {
            ReaderHelper.read300(reader);
        } else {
            ReaderHelper.read100(reader);
        }

        int entitySize = (int) reader.readObject();

        ((FSM) entity).setFsmData(fsmData);

        return entity;
    }

    private static List<Message> readMessages(MaxTypeReader reader) {
        if (28 != reader.readByte()) {
            throw new RuntimeException();
        }

        List<Message> messages = new ArrayList<>();
        int messageCount = (int) reader.readObject(); // 0x14
        for (int i = 0; i < messageCount; i++) {

            int offset = reader.getOffset();

            ReaderHelper.read100(reader);

            int dataSize = (int) reader.readObject();

            Message msg = new Message();

            msg.setMessage((String) reader.readObject());
            msg.setFunctionName((String) reader.readObject());
            msg.setTargetObjectName((String) reader.readObject());

            if (28 != reader.readByte()) {
                throw new RuntimeException();
            }

            int paramsCount = (int) reader.readObject(); // 0x14
            List<String> params = new ArrayList<>();
            for (int j = 0; j < paramsCount; j++) {
                params.add((String) reader.readObject());
            }
            msg.setParams(params);

            int unk1 = (int) reader.readObject(); // 0x0E

            msg.setUnk1((int) reader.readObject()); // 0x11 || 0x03

            if (28 != reader.readByte()) {
                throw new RuntimeException();
            }

            if ((int) reader.readObject() != paramsCount) { // 0x14
                throw new RuntimeException();
            }

            List<Integer> unk3 = new ArrayList<>();
            for (int j = 0; j < paramsCount; j++) {
                unk3.add((int) reader.readObject()); // 0x11
            }

            if (reader.getOffset() - offset != dataSize) {
                throw new RuntimeException("Invalid data size");
            }

            msg.setUnk3(unk3);

            messages.add(msg);
        }

        return messages;
    }
}
