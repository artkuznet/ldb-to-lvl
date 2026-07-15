package com.artkuznet.converter.maxed.helper;

import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed.Mesh;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

public enum LightType {
    WALL_LIGHT_1(4325386381475801589L, getConsumer(0.13)),
    WALL_LIGHT_2(5608666439513540232L, getConsumer(0.16)),
    WALL_LIGHT_3(3873130838351964199L, getConsumer(0.16)),
    WALL_LIGHT_4(5467368387553788836L, getConsumer(0.16)),
    WALL_LIGHT_5(109873541379326905L, getConsumer(0.16)),

    CEILING_LIGHT_1(1828284444412080053L, getConsumer(0.12)),
    CEILING_LIGHT_2(5901167682514518373L, getConsumer(0.18, new LvlPolygon.Color(255, 245, 221))),
    CEILING_LIGHT_3(2234271142888750998L, getConsumer(0.18, new LvlPolygon.Color(255, 247, 225))),
    SECURITY_PAD(
            576318338650542732L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getArea() > 0.04 && p.getArea() < 0.05)
                    .filter(p -> !p.getBitmapName().toLowerCase().contains("dummy"))
                    .forEach(p -> {
                        p.setColor(LvlPolygon.Color.WHITE);
//                        p.setLightIntensity(0.5f);
                    })
    ),
    SECURITY_PAD_1(5522092587850255118L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getArea() > 0.04 && p.getArea() < 0.05)
                    .filter(p -> !p.getBitmapName().toLowerCase().contains("dummy"))
                    .forEach(p -> {
                        p.setColor(LvlPolygon.Color.WHITE);
//                        p.setLightIntensity(0.5f);
                    })
    ),

    BASEMENT_LAMP_PART_1(
            450681776500423730L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getArea() > 0.01)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WARM))
    ),
    BASEMENT_LAMP_PART_2(
            4977642932638294487L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .filter(p -> !p.getBitmapName().toLowerCase().contains("dummy"))
                    .filter(p -> p.getNormal().getY() < 0)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WARM))
    ),


    BASEMENT_LAMP_PART_1_1(
            2457312176549547344L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getArea() > 0.01)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WARM))
    ),
    BASEMENT_LAMP_PART_2_1(
            1506096564715190798L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .filter(p -> !p.getBitmapName().toLowerCase().contains("dummy"))
                    .filter(p -> p.getNormal().getY() < 0)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WARM))
    ),


    EXIT(1134637312547889402L, getConsumer(0.09)),


    CEILING_LAMP(4441521539255256165L, getConsumer(0.18)),
    CEILING_LAMP_1(3003634515268357814L, getConsumer(0.12)),
    TABLE_LAMP_1(5832399858663880214L, getConsumer(0.02)),
    WALL_LAMP_1(8448677673116070026L, getConsumer(0.025, 0.026)),

    PAINKILLER_BOX(1218342504203422251L, mesh -> Arrays.stream(mesh.getPolygons())
            .filter(p -> p.getNormal().getY() < 0)
            .forEach(p -> p.setColor(LvlPolygon.Color.WHITE))
    ),

    SMALL_LIGHT_1(2672633512334233736L, mesh -> Arrays.stream(mesh.getPolygons())
            .filter(p -> p.getNormal().getY() < 0)
            .forEach(p -> p.setColor(LvlPolygon.Color.WHITE))
    ),

    BASEMENT_WALL_LAMP_1(9156694006860667800L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .forEach(p -> {
                        p.setColor(LvlPolygon.Color.WHITE);
                        p.setLightIntensity(0.5f);
                    })
    ),

    LAMP_1(552780747525815209L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .forEach(p -> {
                        p.setColor(LvlPolygon.Color.WARM);
                        p.setLightIntensity(0.2f);
                    })
    );


    private final long checksum;
    private final Consumer<Mesh> processor;

    LightType(long checksum, Consumer<Mesh> processor) {
        this.checksum = checksum;
        this.processor = processor;
    }

    public void processMesh(Mesh mesh) {
        processor.accept(mesh);
    }

    public static Optional<LightType> fromChecksum(long checksum) {
        return Arrays.stream(values())
                .filter(type -> type.checksum == checksum)
                .findFirst();
    }

    private static Consumer<Mesh> getConsumer(double minArea, double maxArea) {
        return getConsumer(minArea, maxArea, LvlPolygon.Color.WHITE);
    }

    private static Consumer<Mesh> getConsumer(double minArea) {
        return getConsumer(minArea, Double.MAX_VALUE, LvlPolygon.Color.WHITE);
    }

    private static Consumer<Mesh> getConsumer(double minArea, LvlPolygon.Color color) {
        return getConsumer(minArea, Double.MAX_VALUE, color);
    }

    private static Consumer<Mesh> getConsumer(double minArea, double maxArea, LvlPolygon.Color color) {
        return mesh -> Arrays.stream(mesh.getPolygons())
                .filter(p -> p.getArea() > minArea && p.getArea() < maxArea)
                .filter(p -> !p.getBitmapName().toLowerCase().contains("dummy"))
                .forEach(p -> p.setColor(color));
    }
}
