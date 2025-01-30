package com.artkuznet.converter.maxed2.entity;

public class TriggerData {

    private boolean player;
    private boolean use;
    private boolean enemy;
    private boolean bullet;
    private boolean lookAt;
    private boolean visibility;
    private String activatorsUseAnimation = "Default";

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public void setEnemy(boolean enemy) {
        this.enemy = enemy;
    }

    public void setBullet(boolean bullet) {
        this.bullet = bullet;
    }

    public void setLookAt(boolean lookAt) {
        this.lookAt = lookAt;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setActivatorsUseAnimation(String activatorsUseAnimation) {
        this.activatorsUseAnimation = activatorsUseAnimation;
    }

    public boolean isPlayer() {
        return player;
    }

    public boolean isUse() {
        return use;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public boolean isBullet() {
        return bullet;
    }

    public boolean isLookAt() {
        return lookAt;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public String getActivatorsUseAnimation() {
        return activatorsUseAnimation;
    }

}
