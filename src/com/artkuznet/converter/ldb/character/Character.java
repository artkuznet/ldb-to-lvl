package com.artkuznet.converter.ldb.character;

import com.artkuznet.converter.ldb.fsm.FSMMessageContainer;
import com.artkuznet.converter.ldb.property.EntityProperties;

public class Character {
    private String sharedName;
    private EntityProperties properties;
    private String characterName;
    private FSMMessageContainer startupBefore;
    private FSMMessageContainer onDeathBefore;
    private FSMMessageContainer onActivateBefore;
    private FSMMessageContainer onSpecialBefore;

    public Character(
            String sharedName,
            EntityProperties properties,
            String characterName,
            FSMMessageContainer startupBefore,
            FSMMessageContainer onDeathBefore,
            FSMMessageContainer onActivateBefore,
            FSMMessageContainer onSpecialBefore
    ) {
        this.sharedName = sharedName;
        this.properties = properties;
        this.characterName = characterName;
        this.startupBefore = startupBefore;
        this.onDeathBefore = onDeathBefore;
        this.onActivateBefore = onActivateBefore;
        this.onSpecialBefore = onSpecialBefore;
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

    public String getCharacterName() {
        return characterName;
    }

    public FSMMessageContainer getStartupBefore() {
        return startupBefore;
    }

    public FSMMessageContainer getOnDeathBefore() {
        return onDeathBefore;
    }

    public FSMMessageContainer getOnActivateBefore() {
        return onActivateBefore;
    }

    public FSMMessageContainer getOnSpecialBefore() {
        return onSpecialBefore;
    }
}
