package com.waifu.ui;

import com.waifu.model.Waifu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImageRepository {
    private final Map<String, ImageIcon> cache = new HashMap<>();

    public ImageIcon avatar(Waifu waifu, int width, int height) {
        String key = waifu.getName().toLowerCase() + "-" + width + "x" + height;
        String resourceKey = waifu.getName().toLowerCase();
        return cache.computeIfAbsent(key, ignored -> load(resourceKey, width, height));
    }

    private ImageIcon load(String key, int width, int height) {
        String path = "/images/" + key + ".png";
        try (InputStream input = ImageRepository.class.getResourceAsStream(path)) {
            if (input != null) {
                BufferedImage image = ImageIO.read(input);
                Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (IOException ignored) { }
        return placeholder(key, width, height);
    }

    private ImageIcon placeholder(String key, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(25, 31, 54));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(12, width / 8)));
        String text = key.substring(0, 1).toUpperCase();
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (width - fm.stringWidth(text)) / 2, height / 2);
        g.dispose();
        return new ImageIcon(image);
    }
}
