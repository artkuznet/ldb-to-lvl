package com.artkuznet.converter.maxed;

public interface Dynamic {

    String KEYFRAME_0 = "Keyframe 0";

    DynamicMesh.DynamicData getDynamicData();

    String getDefaultKeyframe();

    String getUnknownKeyframe();

}
