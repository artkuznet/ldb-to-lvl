package com.artkuznet.converter.ldb.fsm;

import com.artkuznet.converter.ldb.property.EntityProperties;

public class LdbFSM {

    private String sharedName;
    private EntityProperties properties;

    private FSMStateContainer states;

    private FSMMessageContainer startupBefore;

    private FSMMessageContainer startupAfter;

    private FSMEventContainer stateSwitch;

    private FSMEventContainer stringSpecific;

    private FSMEventContainer entitySpecific;

    public LdbFSM(
            String sharedName,
            EntityProperties properties,
            FSMStateContainer states,
            FSMMessageContainer startupBefore,
            FSMMessageContainer startupAfter,
            FSMEventContainer stateSwitch,
            FSMEventContainer stringSpecific,
            FSMEventContainer entitySpecific
    ) {
        this.sharedName = sharedName;
        this.properties = properties;
        this.states = states;
        this.startupBefore = startupBefore;
        this.startupAfter = startupAfter;
        this.stateSwitch = stateSwitch;
        this.stringSpecific = stringSpecific;
        this.entitySpecific = entitySpecific;
    }

    public String getSharedName() {
        return sharedName;
    }

    public String getShortName() {
        return sharedName.substring(sharedName.lastIndexOf("::") + 2);
    }

    public String getRoomName() {
        return sharedName.substring(0, sharedName.substring(2).indexOf("::") + 2);
    }

    public EntityProperties getProperties() {
        return properties;
    }

    public FSMStateContainer getStates() {
        return states;
    }

    public FSMMessageContainer getStartupBefore() {
        return startupBefore;
    }

    public FSMMessageContainer getStartupAfter() {
        return startupAfter;
    }

    public FSMEventContainer getStateSwitch() {
        return stateSwitch;
    }

    public FSMEventContainer getStringSpecific() {
        return stringSpecific;
    }

    public FSMEventContainer getEntitySpecific() {
        return entitySpecific;
    }
}
