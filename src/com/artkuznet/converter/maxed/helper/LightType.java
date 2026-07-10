package com.artkuznet.converter.maxed.helper;

import com.artkuznet.converter.maxed.LvlPolygon;
import com.artkuznet.converter.maxed.Mesh;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

public enum LightType {
    STREET_LIGHT_1(
            4325386381475801589L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getArea() > 0.13)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WHITE))
    ),
    STREET_LIGHT_2(
            5608666439513540232L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getArea() > 0.16)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WHITE))),
    STREET_LIGHT_3(
            3873130838351964199L,
            mesh -> Arrays.stream(mesh.getPolygons())
                    .filter(p -> p.getArea() > 0.16)
                    .forEach(p -> p.setColor(LvlPolygon.Color.WHITE))
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
}
