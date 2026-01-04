package com.artkuznet.converter.lv2.writer;

import com.artkuznet.converter.*;
import com.artkuznet.converter.Number;
import com.artkuznet.converter.lv2.Block;
import com.artkuznet.converter.maxed2.entity.Enemy;
import com.artkuznet.converter.maxed2.entity.Trigger;
import com.artkuznet.converter.maxed2.entity.fsm.*;
import com.artkuznet.converter.maxed2.entity.mesh.DynamicMesh;
import com.artkuznet.converter.maxed2.entity.mesh.DynamicTriangleMesh;
import com.artkuznet.converter.maxed2.entity.prefab.PrefabParent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FSMWriter {

    public static void write(MaxTypeWriter writer, FSM fsm, int entityDataSize) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(fsmDataToBytes(fsm.getFsmData()));

        if (fsm instanceof DynamicMesh) {
            buffer.write(Block.X10C);
        } else if (fsm instanceof PrefabParent || fsm instanceof DynamicTriangleMesh) {
            buffer.write(Block.X300);
        } else {
            buffer.write(Block.X100);
        }

        int fix = 13;
        if (fsm instanceof Trigger) {
            fix -= 35;
        }
        if (fsm instanceof Enemy) {
            fix -= (26 + ((Enemy) fsm).getGroup().length());
        }

        buffer.write(fix + entityDataSize);

        writer.write(new UNumber(buffer.getSize()));
        writer.write(WriterHelper.toBytes(Block.X100, Collections.singletonList(buffer.toBytes()), false));
    }

    private static byte[] fsmDataToBytes(FsmData data) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.writeByte((byte) 29);
        buffer.write(new Number(data.getStateNames().size()));
        buffer.write(data.getStateNames());
        buffer.write(data.getDefaultStateName());
        buffer.writeByte((byte) 29);
        buffer.write(new Number(data.getCustomEventNames().size()));
        buffer.write(data.getCustomEventNames());
        buffer.writeBytes(handlersToBytes(data.getHandlers()));
        buffer.writeBytes(new byte[]{0x0E, (byte) (data.getUnk1() ? 1 : 0)});
        buffer.writeBytes(new byte[]{0x0E, (byte) (data.getUnk2() ? 1 : 0)});
        buffer.write(timersToBytes(data.getTimers()));

        return buffer.toBytes();
    }

    private static byte[] timersToBytes(List<Timer> timers) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.writeByte((byte) 31);
        buffer.write(new Number(timers.size()));
        for (Timer timer : timers) {
            buffer.write(timer.getName());
            buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                    new byte[]{0x0E, (byte) (timer.getTypeToggle() ? 1 : 0)},
                    timer.getLength()
            )));
        }

        return buffer.toBytes();
    }

    private static byte[] handlersToBytes(List<Handler> handlers) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.writeByte((byte) 31);
        buffer.write(new Number(handlers.size()));
        for (Handler handler : handlers) {
            buffer.write(handler.getName());
            buffer.writeBytes(messagesToBytes(handler.getBeforeMessages()));
            buffer.writeByte((byte) 31);
            buffer.write(new Number(handler.getStateMessages().size()));
            for (String stateMessageName : handler.getStateMessages().keySet()) {
                buffer.write(stateMessageName);
                buffer.writeBytes(messagesToBytes(handler.getStateMessages().get(stateMessageName)));
            }
            buffer.writeBytes(messagesToBytes(handler.getAfterMessages()));
        }

        return buffer.toBytes();
    }

    private static byte[] messagesToBytes(List<Message> messages) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.writeByte((byte) 28);
        buffer.write(new Number(messages.size()));
        for (Message message : messages) {
            buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                    message.getMessage(),
                    message.getFunctionName(),
                    message.getTargetObjectName(),
                    new byte[]{(byte) 28},
                    new Number(message.getParams().size()),
                    message.getParams(),
                    new byte[]{0x0E, (byte) (message.getTargetObjectName().isEmpty() ? 1 : 0)},
                    new UNumber(message.getUnk1()),
                    new byte[]{(byte) 28},
                    new Number(message.getParams().size()),
                    message.getUnk3().stream().map(UNumber::new).collect(Collectors.toList())
            )));
        }

        return buffer.toBytes();
    }
}
