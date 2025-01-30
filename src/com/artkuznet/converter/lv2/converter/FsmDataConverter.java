package com.artkuznet.converter.lv2.converter;

import com.artkuznet.converter.ldb2.dynamicmesh.DynamicMeshAnimation;
import com.artkuznet.converter.ldb2.fsm.FSM;
import com.artkuznet.converter.ldb2.fsm.FSMTimer;
import com.artkuznet.converter.maxed2.entity.fsm.FsmData;
import com.artkuznet.converter.maxed2.entity.fsm.Timer;

import java.util.ArrayList;
import java.util.List;

public class FsmDataConverter {

    public static FsmData convert(FSM fsm) {
        return convert(fsm, new ArrayList<>());
    }

    public static FsmData convert(FSM fsm, List<DynamicMeshAnimation> animations) {

        FsmData fsmData = new FsmData();

        fsmData.setDefaultStateName(fsm.getDefaultState());
        fsmData.setStateNames(fsm.getStateNames());
        fsmData.setCustomEventNames(new ArrayList<>(fsm.getCustomEvents().keySet()));

        fsmData.addHandler(HandlerConverter.convert(
                "Startup",
                fsm.getOnStartup(),
                fsm.getStateNames()
        ));

        fsm.getCustomEvents()
                .forEach((eventName, fsmCode) ->
                        fsmData.addHandler(
                                HandlerConverter.convert(
                                        String.format("FSM_Send(%s)", eventName),
                                        fsmCode,
                                        fsm.getStateNames()
                                )
                        )
                );

        fsm.getEvents()
                .forEach((eventName, fsmCode) ->
                        fsmData.addHandler(
                                HandlerConverter.convert(
                                        eventName,
                                        fsmCode,
                                        fsm.getStateNames()
                                )
                        )
                );

        for (FSMTimer timer : fsm.getTimers()) {
            Timer t = new Timer();
            t.setName(timer.getName());
            t.setLength(timer.getLength());
            t.setTypeToggle(1 == timer.getIsRealTime());

            fsmData.addTimer(t);

            fsmData.addHandler(HandlerConverter.convert(
                            String.format("OnStartTimer(%s)", timer.getName()),
                            timer.getStartTimer(),
                            fsm.getStateNames()
                    )
            );

            fsmData.addHandler(HandlerConverter.convert(
                            String.format("OnEndTimer(%s)", timer.getName()),
                            timer.getEndTimer(),
                            fsm.getStateNames()
                    )
            );
        }

        for (DynamicMeshAnimation animation : animations) {
            fsmData.addHandler(HandlerConverter.convert(animation));
        }

        return fsmData;
    }
}
