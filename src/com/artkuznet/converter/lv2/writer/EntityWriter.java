package com.artkuznet.converter.lv2.writer;

import com.artkuznet.converter.*;
import com.artkuznet.converter.Number;
import com.artkuznet.converter.lv2.Block;
import com.artkuznet.converter.maxed2.entity.*;
import com.artkuznet.converter.maxed2.entity.fsm.FSM;
import com.artkuznet.converter.maxed2.entity.fsm.FloatingFSM;
import com.artkuznet.converter.maxed2.entity.mesh.*;
import com.artkuznet.converter.maxed2.entity.point.AIN;
import com.artkuznet.converter.maxed2.entity.point.JumpPoint;
import com.artkuznet.converter.maxed2.entity.point.WayPoint;
import com.artkuznet.converter.maxed2.entity.prefab.Prefab;
import com.artkuznet.converter.maxed2.entity.prefab.PrefabParent;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class EntityWriter {

    public static void write(MaxTypeWriter writer, Entity entity) {
        writer.write(getEntityType(entity));

        MaxTypeWriter buffer = new MaxTypeWriter();

        if (entity instanceof Player) {
            buffer.write("PLAYER_GROUP");
        }
        if (entity instanceof Mesh) {
            MeshWriter.write(buffer, (Mesh) entity);
        }
        if (entity instanceof PolyGroup) {
            buffer.writeBytes(polygroupToBytes((PolyGroup) entity));
        }
        if (entity instanceof Portal) {
            buffer.write(WriterHelper.toBytes(Block.X300, Arrays.asList(
                    new UNumber(((Portal) entity).getPolygonIndex()),
                    ((Portal) entity).isIgnoreInGISLighting(),
                    ((Portal) entity).isAlwaysClosed()
            )));
        }
        if (entity instanceof Flare) {
            buffer.writeBytes(flareToBytes((Flare) entity));
        }
        if (entity instanceof LevelItem) {
            buffer.writeBytes(levelItemToBytes((LevelItem) entity));
        }
        if (entity instanceof Enemy) {
            buffer.writeBytes(enemyToBytes((Enemy) entity));
        }
        if (entity instanceof DynamicPointlight) {
            buffer.writeBytes(dynamicPointLightToBytes((DynamicPointlight) entity));
        }
        if (entity instanceof RadiosityLight) {
            buffer.writeBytes(radiosityLightToBytes((RadiosityLight) entity));
        }
        if (entity instanceof Trigger) {
            buffer.writeBytes(triggerToBytes((Trigger) entity));
        }
        if (entity instanceof FloatingFSM) {
            buffer.write(entity.getRadius());
        }
        if (entity instanceof VolumeLightingBox) {
            buffer.writeBytes(volumeLightingBoxToBytes((VolumeLightingBox) entity));
        }
        if (entity instanceof PrefabParent) {
            buffer.write(((PrefabParent) entity).getType());
            buffer.write(((PrefabParent) entity).isOpened());
            buffer.write(entity.getRadius());
        }
        if (entity instanceof TriangleMesh) {
            TriangleMeshWriter.write(buffer, (TriangleMesh) entity);
        }

        writer.write(WriterHelper.toBytes(Block.X400, Arrays.asList(
                new UInt(entity.getUnk1()),
                localMatrixToBytes(entity.getLocalMatrix()),
                entity.getMinPoint(),
                entity.getMaxPoint(),
                entity.getRadius(),
                !entity.isHidden(),
                entity.isExcludeFromGame(),
                entity.isExcludeFromLighting(),
                entity.isEnableExportRegrouping(),
                entity.isGameplayCritical(),
                entity.getName(),
                entity instanceof FSM,
                (entity instanceof FSM)
                        ? fsmToBytes((FSM) entity, buffer.getSize())
                        : new byte[]{}
        ), !(entity instanceof FSM)));

        writer.writeBytes(buffer.toBytes());

        writer.write(new UNumber(entity.getChildEntities().size()));
        for (Entity child : entity.getChildEntities()) {
            write(writer, child);
        }
    }

    private static byte[] polygroupToBytes(PolyGroup polyGroup) {
        MaxTypeWriter buffer = new MaxTypeWriter();
        buffer.write(WriterHelper.toBytes(Block.X200, Arrays.asList(
                new UNumber(polyGroup.getPolygonIndices().size()),
                polyGroup.getPolygonIndices().stream().map(UNumber::new).collect(Collectors.toList()),
                polyGroup.isSmoothLightmaps(),
                polyGroup.isSmoothGeometry(),
                polyGroup.getMaxAngle(),
                polyGroup.getMaxEdge(),
                polyGroup.isFreezeLightmaps(),
                polyGroup.isRayTracing()
        )));
        return buffer.toBytes();
    }

    private static byte[] flareToBytes(Flare flare) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                flare.getType(),
                flare.getRadius()
        )));

        buffer.write(WriterHelper.toBytes(Block.X100, Collections.emptyList()));

        return buffer.toBytes();
    }

    private static byte[] levelItemToBytes(LevelItem levelItem) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                levelItem.getType(),
                levelItem.getRadius()
        )));

        return buffer.toBytes();
    }

    private static byte[] enemyToBytes(Enemy enemy) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(enemy.getType());
        buffer.write(enemy.getRadius());

        buffer.write(WriterHelper.toBytes(Block.X200, Arrays.asList(
                enemy.getGroup(),
                enemy.getActivatorsUseAnimation()
        )));

        return buffer.toBytes();
    }

    private static byte[] dynamicPointLightToBytes(DynamicPointlight dynamicPointlight) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                dynamicPointlight.getType(),
                dynamicPointlight.getRadius()
        )));

        buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                dynamicPointlight.getColor().getR(),
                dynamicPointlight.getColor().getG(),
                dynamicPointlight.getColor().getB(),
                dynamicPointlight.getColor().getA(),
                dynamicPointlight.getIntensity(),
                dynamicPointlight.getFalloff()
        )));

        return buffer.toBytes();
    }

    private static byte[] radiosityLightToBytes(RadiosityLight radiosityLight) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                new byte[]{8, (byte) radiosityLight.getColor().getR()},
                new byte[]{8, (byte) radiosityLight.getColor().getG()},
                new byte[]{8, (byte) radiosityLight.getColor().getB()},
                new byte[]{8, (byte) radiosityLight.getColor().getA()},
                radiosityLight.getIntensity(),
                radiosityLight.getHotspotAngle(),
                radiosityLight.getFalloffAngle(),
                new UNumber(radiosityLight.getRadiosityLightIndex())
        )));

        return buffer.toBytes();
    }

    private static byte[] triggerToBytes(Trigger trigger) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(trigger.getType());
        buffer.write(trigger.getRadius());

        TriggerData data = trigger.getData();

        buffer.write(WriterHelper.toBytes(Block.X300, Arrays.asList(
                data.isPlayer(),
                data.isUse(),
                data.isEnemy(),
                data.isBullet(),
                data.isLookAt(),
                data.isVisibility(),
                data.getActivatorsUseAnimation()
        )));

        return buffer.toBytes();
    }

    private static byte[] volumeLightingBoxToBytes(VolumeLightingBox volumeLightingBox) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(WriterHelper.toBytes(Block.X100, Arrays.asList(
                volumeLightingBox.getWidth(),
                volumeLightingBox.getHeight(),
                volumeLightingBox.getDepth(),
                new Number(volumeLightingBox.getResolution())
        )));

        return buffer.toBytes();
    }

    public static byte getEntityType(Entity entity) {
        if (entity instanceof TriangleMesh) {
            return 117;
        }
        if (entity instanceof VolumeLightingBox) {
            return 120;
        }
        if (entity instanceof PrefabParent) {
            return 104;
        }
        if (entity instanceof Prefab) {
            return 115;
        }
        if (entity instanceof AIN) {
            return 118;
        }
        if (entity instanceof DynamicMesh) {
            return 100;
        }
        if (entity instanceof Mesh) {
            return 100;
        }
        if (entity instanceof PolyGroup) {
            return 112;
        }
        if (entity instanceof Portal) {
            return 114;
        }
        if (entity instanceof Trigger) {
            return 106;
        }
        if (entity instanceof RadiosityLight) {
            return 113;
        }
        if (entity instanceof WayPoint) {
            return 107;
        }
        if (entity instanceof JumpPoint) {
            return 108;
        }
        if (entity instanceof LevelItem) {
            return 109;
        }
        if (entity instanceof Player) {
            return 116;
        }
        if (entity instanceof WorldGroup) {
            return 103;
        }
        if (entity instanceof FloatingFSM) {
            return 105;
        }
        if (entity instanceof DynamicPointlight) {
            return 111;
        }
        if (entity instanceof Enemy) {
            return 110;
        }
        if (entity instanceof Flare) {
            return 119;
        }

        throw new RuntimeException("unknown entity");
    }

    public static byte[] localMatrixToBytes(double[][] m) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        buffer.write(m[3][0]);
        buffer.write(m[3][1]);
        buffer.write(m[3][2]);

        buffer.write(m[0][0]);
        buffer.write(m[0][1]);
        buffer.write(m[0][2]);

        buffer.write(m[1][0]);
        buffer.write(m[1][1]);
        buffer.write(m[1][2]);

        buffer.write(m[2][0]);
        buffer.write(m[2][1]);
        buffer.write(m[2][2]);

        return buffer.toBytes();
    }

    private static byte[] fsmToBytes(FSM fsm, int entityDataSize) {
        MaxTypeWriter buffer = new MaxTypeWriter();

        FSMWriter.write(buffer, fsm, entityDataSize);

        return buffer.toBytes();
    }
}
