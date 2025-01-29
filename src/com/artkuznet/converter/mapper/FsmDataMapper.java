package com.artkuznet.converter.mapper;

import com.artkuznet.converter.ldb.character.Character;
import com.artkuznet.converter.ldb.dynamicmesh.LdbDynamicMesh;
import com.artkuznet.converter.ldb.fsm.*;
import com.artkuznet.converter.maxed.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FsmDataMapper {

    private Map<String, String> objectNames;

    private static final Pattern PATTERN = Pattern.compile("^([A-z0-9]+)\\(([A-z0-9,.\": +-]{0,}+)\\);$");

    public FsmDataMapper(Map<String, String> objectNames) {
        this.objectNames = objectNames;
    }

    public FloatingFSM.FSMData toFSMData(MaxObject object, Character ldbCharacter) {
        FloatingFSM.FSMData fsmData = new FloatingFSM.FSMData();

        fsmData.enemyData = true;

        String oldObjectName = object.fullName;

        List<FloatingFSM.FSMData.Message> onActivateMessages = getMessages(ldbCharacter.getOnActivateBefore().getList(), oldObjectName);
        List<FloatingFSM.FSMData.Message> onDeathMessages = getMessages(ldbCharacter.getOnDeathBefore().getList(), oldObjectName);
        List<FloatingFSM.FSMData.Message> onSpecialMessages = getMessages(ldbCharacter.getOnSpecialBefore().getList(), oldObjectName);
        List<FloatingFSM.FSMData.Message> onStartupMessages = getMessages(ldbCharacter.getStartupBefore().getList(), oldObjectName);

        fsmData.messageHandlers.add(getEnemyHandler("OnActivate", onActivateMessages));
        fsmData.messageHandlers.add(getEnemyHandler("OnDeath", onDeathMessages));
        fsmData.messageHandlers.add(getEnemyHandler("OnSpecial", onSpecialMessages));
        fsmData.messageHandlers.add(getEnemyHandler("Startup", onStartupMessages));

        return fsmData;
    }

    private FloatingFSM.FSMData.MessageHandler getEnemyHandler(String handlerName, List<FloatingFSM.FSMData.Message> messages) {
        FloatingFSM.FSMData.MessageHandler handler = new FloatingFSM.FSMData.MessageHandler();

        handler.name = handlerName;
        handler.sendBefore = messages;

        return handler;
    }


    public FloatingFSM.FSMData toFSMData(MaxObject object, LdbFSM ldbFSM, LdbDynamicMesh ldbDynamicMesh) {
        FloatingFSM.FSMData fsmData = new FloatingFSM.FSMData();

        String oldObjectName = object.fullName;
        if (object instanceof Dynamic) {
            oldObjectName = oldObjectName.replace(".DO", "");
        }
        if (object instanceof LvlTrigger) {
            oldObjectName = oldObjectName.replace(".TRIGGER", "");
        }

        fsmData.states = ldbFSM.getStates().getList();
        fsmData.defaultState = ldbFSM.getStates().getDefaultState();

        if (object instanceof Dynamic) {
            String finalOldObjectName = oldObjectName;
            ldbDynamicMesh.getAnimations().getList().forEach(animation -> {
                FloatingFSM.FSMData.MessageHandler handler = new FloatingFSM.FSMData.MessageHandler();

                handler.name = String.format("Animation(%s)", animation.getAnimationName());
                handler.stateSpecific.put("Enter1st", getMessages(animation.getReturningFirstFrame().getList(), finalOldObjectName));
                handler.stateSpecific.put("Enter2nd", getMessages(animation.getReachingSecondFrame().getList(), finalOldObjectName));
                handler.stateSpecific.put("Leave1st", getMessages(animation.getLeavingFirstFrame().getList(), finalOldObjectName));

                fsmData.messageHandlers.add(handler);
            });
        }

        if (!(object instanceof LvlTrigger)) {
            fsmData.messageHandlers.addAll(getHandlers(ldbFSM.getEntitySpecific(), oldObjectName, "%s"));
        }

        fsmData.messageHandlers.addAll(getHandlers(ldbFSM.getStringSpecific(), oldObjectName, "FSM_Send(%s)"));
        fsmData.messageHandlers.addAll(getHandlers(ldbFSM.getStateSwitch(), oldObjectName, "FSM_Switch(%s)"));

        fsmData.customStrings = ldbFSM.getStringSpecific().getList().stream()
                .map(FSMEvent::getStateName)
                .collect(Collectors.toList());

        FloatingFSM.FSMData.MessageHandler handlerStartup = new FloatingFSM.FSMData.MessageHandler();
        handlerStartup.name = "Startup";
        handlerStartup.sendBefore = getMessages(ldbFSM.getStartupBefore().getList(), oldObjectName);
        handlerStartup.sendAfter = getMessages(ldbFSM.getStartupAfter().getList(), oldObjectName);

        Map<String, List<FloatingFSM.FSMData.Message>> m = new LinkedHashMap<>();

        if (!fsmData.defaultState.isEmpty()) {
            m.put(fsmData.defaultState, new ArrayList<>());
        }

        fsmData.messageHandlers.add(handlerStartup);

        if (object instanceof LvlTrigger) {
            fsmData.messageHandlers.addAll(getHandlers(ldbFSM.getEntitySpecific(), oldObjectName, "%s"));
        }

        return fsmData;
    }

    private List<FloatingFSM.FSMData.MessageHandler> getHandlers(FSMEventContainer eventContainer, String objectName, String stateNameFormat) {
        return eventContainer.getList().stream().map(event -> {
            FloatingFSM.FSMData.MessageHandler handler = new FloatingFSM.FSMData.MessageHandler();

            handler.setName(String.format(stateNameFormat, event.getStateName()));

            handler.sendBefore = getMessages(event.getBefore().getList(), objectName);
            handler.sendAfter = getMessages(event.getAfter().getList(), objectName);

            event.getStateSpecific().getList().forEach(specMsg ->
                    handler.stateSpecific.put(
                            specMsg.getStateName(),
                            getMessages(specMsg.getMessages().getList(), objectName)
                    )
            );

            return handler;
        }).collect(Collectors.toList());
    }

    private List<FloatingFSM.FSMData.Message> getMessages(List<String> messages, String objectName) {
        return messages.stream().map(m -> getMessage(m, objectName)).collect(Collectors.toList());
    }

    private FloatingFSM.FSMData.Message getMessage(String message, String objectName) {
        FloatingFSM.FSMData.Message fsmMsg = new FloatingFSM.FSMData.Message();

        if (!message.contains("->")) {
            throw new RuntimeException();
        }

        String[] objWithFunc = message.split("->");
        if (objWithFunc.length != 2) {
            throw new RuntimeException();
        }

        String targetObjectName = objWithFunc[0];
        String functionWithParams = objWithFunc[1];

        java.util.regex.Matcher matcher = PATTERN.matcher(functionWithParams);
        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new RuntimeException("Pattern error: " + functionWithParams);
        }

        String functionName = matcher.group(1);
        List<String> params = matcher.group(2).isEmpty()
                ? Collections.emptyList()
                : Arrays.stream(matcher.group(2).split(",")).map(String::trim).collect(Collectors.toList());

        if (targetObjectName.equalsIgnoreCase("player")
                || targetObjectName.equalsIgnoreCase("activator")
                || targetObjectName.equalsIgnoreCase("maxpayne_hudmode")
                || targetObjectName.equalsIgnoreCase("maxpayne_graphicnovelmode")
                || targetObjectName.equalsIgnoreCase("x_modeswitch")
                || targetObjectName.equalsIgnoreCase("maxpayne_gamemode")
        ) {
            if (functionName.equalsIgnoreCase("GM_EnableExit") || functionName.equalsIgnoreCase("GM_EnableExitAIActivation")) {
                String exitName = params.get(0).substring(params.get(0).substring(2).indexOf("::") + 4);
                targetObjectName = params.get(0).substring(0, params.get(0).lastIndexOf("::"));
                functionName = functionName.substring(3);
                fsmMsg.message = targetObjectName + "->" + String.format("%s(%s, %s);", functionName, params.get(1), exitName);
                fsmMsg.targetObjectName = targetObjectName;
                fsmMsg.functionName = functionName;
                fsmMsg.params = mapParams(Arrays.asList(params.get(1), exitName));
            } else {
                fsmMsg.message = targetObjectName + "->" + functionWithParams;
                fsmMsg.targetObjectName = "";
                fsmMsg.functionName = targetObjectName + "->" + functionName;
                fsmMsg.params = mapParams(params);
            }
        } else {
            String newTargetName = getNewObjectName(targetObjectName);
            String newObjectName = getNewObjectName(objectName);

            fsmMsg.message = getMessageTargetName(newObjectName, newTargetName) + "->" + functionWithParams;
            fsmMsg.targetObjectName = newTargetName;
            fsmMsg.functionName = functionName;
            fsmMsg.params = mapParams(params);
        }

        if (fsmMsg.message.length() > 255) {
            throw new RuntimeException(fsmMsg.message);
        }

        return fsmMsg;
    }

    private List<String> mapParams(List<String> params) {
        return params.stream().map(p -> {
            if (!p.contains("::")) {
                return p;
            }

            boolean quotes = p.startsWith("\"") && p.endsWith("\"");

            if (quotes) {
                p = p.substring(1);
                p = p.substring(0, p.length() - 1);
            }

            List<String> chunks = new ArrayList<>(Arrays.stream(p.substring(2).split("::")).collect(Collectors.toList()));
            String newP = "::" + chunks.get(0) + "::";
            chunks.remove(0);
            newP += String.join("_", chunks);

            if (quotes) {
                newP = String.format("\"%s\"", newP);
            }

            return newP;

        }).collect(Collectors.toList());
    }

    private String getMessageTargetName(String objectName, String targetName) {
        if (!objectName.contains("::")) {
            throw new RuntimeException();
        }

        if (objectName.equals(targetName)) {
            return "this";
        }

        return targetName;
    }

    private String getNewObjectName(String objectName) {
        String newName1 = objectNames.get(objectName);
        String newName2 = objectNames.get(objectName + ".DO");
        String newName3 = objectNames.get(objectName + ".TRIGGER");

        String newName = newName1 != null ? newName1 : (newName2 != null ? newName2 : newName3);

        if (newName == null) {
            throw new RuntimeException("New object name not found: " + objectName);
        }
        if (newName.endsWith(".DO")) {
            newName = newName.substring(0, newName.length() - 3);
        }
        if (newName.endsWith(".TRIGGER")) {
            newName = newName.substring(0, newName.length() - 8);
        }

        return newName;
    }
}
