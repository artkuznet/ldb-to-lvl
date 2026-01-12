package com.artkuznet.converter.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class DdsToJpgConverter {

    public static byte[] convert(byte[] ddsBytes) throws Exception {
        BufferedImage src;

        try (ByteArrayInputStream in = new ByteArrayInputStream(ddsBytes)) {
            src = ImageIO.read(in);
        }

        if (src == null) {
            throw new IllegalArgumentException("DDS image could not be read");
        }

        BufferedImage rgbImage = new BufferedImage(
                src.getWidth(),
                src.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g = rgbImage.createGraphics();
        g.setComposite(AlphaComposite.Src);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, src.getWidth(), src.getHeight());

        g.drawImage(src, 0, 0, null);
        g.dispose();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(rgbImage, "jpg", out);
            return out.toByteArray();
        }
    }
}
