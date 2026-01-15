package com.artkuznet.converter.util;

import com.artkuznet.converter.maxed2.entity.Entity;
import com.artkuznet.converter.maxed2.entity.Player;
import com.artkuznet.converter.maxed2.entity.fsm.FsmData;
import com.artkuznet.converter.maxed2.entity.fsm.Message;
import com.artkuznet.converter.maxed2.entity.mesh.Mesh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FsmMessagesFixer {

    public static void updateFsmMessages(List<Mesh> rooms, Player player) {
        Map<String, String> fsmNamesMap = new HashMap<>();
        rooms.forEach(room -> room.getAllFsmChildEntities().forEach(entity -> {
            if (entity.getLdb2FsmName() == null) {
                throw new RuntimeException();
            }
            if (!fsmNamesMap.containsKey(entity.getLdb2FsmName())) {
                String newName = entity.getFullName();
                if (!newName.equals(entity.getLdb2FsmName())) {
                    fsmNamesMap.put(entity.getLdb2FsmName(), newName);
                }
            }
        }));

        List<String> fsmDuplicates = fsmNamesMap.values().stream()
                .collect(Collectors.groupingBy(value -> value, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!fsmDuplicates.isEmpty()) {
            throw new RuntimeException("fsm duplicates found");
        }

        rooms.forEach(room -> room.getChildEntities().forEach(e -> {
            fixFsmMessages(e, fsmNamesMap);
            for (Entity child : e.getChildEntities()) {
                fixFsmMessages(child, fsmNamesMap);
            }
        }));

        fixFsmMessages(player, fsmNamesMap);
    }

    private static void fixFsmMessages(Entity entity, Map<String, String> fsmNamesMap) {
        if (!(entity instanceof com.artkuznet.converter.maxed2.entity.fsm.FSM)) {
            return;
        }

        FsmData fsmData = ((com.artkuznet.converter.maxed2.entity.fsm.FSM) entity).getFsmData();

        fsmData.getHandlers().forEach(handler -> {
            updateHandlerMessages(handler.getBeforeMessages(), entity, fsmNamesMap);
            updateHandlerMessages(handler.getAfterMessages(), entity, fsmNamesMap);
            handler.getStateMessages().forEach((stateName, messages) -> updateHandlerMessages(messages, entity, fsmNamesMap));
        });

        for (Entity child : entity.getChildEntities()) {
            fixFsmMessages(child, fsmNamesMap);
        }
    }

    private static void updateHandlerMessages(List<Message> messages, Entity entity, Map<String, String> fsmNamesMap) {
        messages.forEach(message -> {
            String objName = message.getMessage().substring(0, message.getMessage().indexOf("->"));
            String newObjName = fsmNamesMap.get(objName);

            if (newObjName != null) {

//                if (newObjName.toLowerCase().endsWith(".ai")) {
//                    newObjName = newObjName.substring(0, newObjName.length() - 3);
//                }

                message.setMessage(message.getMessage().replace(
                        objName,
                        newObjName.equals(entity.getFullName()) ? "this" : newObjName
                ));
            } else if (objName.toLowerCase().endsWith(".ai")) {
                message.setMessage(message.getMessage().replace(objName, objName.substring(0, objName.length() - 3)));


                // todo refactor
                if (message.getMessage().contains("AI_AddCommand") && message.getMessage().contains("PATROL")) {

                    String[] func = message.getMessage().split("->");

                    String f = func[1].replace("AI_AddCommand(", "")
                            .replace(");", "")
                            .replace("\"", "")
                            .replace(" ", "");

                    String[] p = f.split(",");

                    String[] wp = p[2].split("&");

                    String newMsg = func[0] + "->" + "AI_AddPatrolCommand(" + String.join(", ", wp) + ");";

                    message.setMessage(newMsg);
                }

            }

            // todo remove
            message.setMessage(message.getMessage()

                    .replace(
                            ":d_forklift_parent::e02",
                            ":d_forklift_parent::d_forklift_parent_e02"
                    )
                    .replace(
                            "::ware_cupboard_green::painkiller",
                            "::ware_cupboard_green_painkiller"
                    )
                    .replace(
                            "::prefab::Item->",
                            "_item->"
                    )
            );


        });
    }
}
