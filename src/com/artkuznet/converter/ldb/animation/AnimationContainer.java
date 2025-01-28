package com.artkuznet.converter.ldb.animation;

import java.util.ArrayList;
import java.util.List;

public class AnimationContainer {
    private List<Animation> animations = new ArrayList<>();

    public void add(Animation animation) {
        animations.add(animation);
    }

    public List<Animation> getList() {
        return animations;
    }
}
