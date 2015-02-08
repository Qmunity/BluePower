package com.bluepowermod.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class Layout {

    private String path = null;

    private BufferedImage layout;
    private Map<Integer, BufferedImage> colorMaps = new HashMap<Integer, BufferedImage>();
    private Map<Integer, SimplifiedLayout> simplificationMaps = new HashMap<Integer, SimplifiedLayout>();

    private LayoutConfiguration config = null;

    private Layout parent = null;
    private Map<Integer, Layout> subLayouts = null;

    public Layout(String path) {

        this.path = path;
        subLayouts = new HashMap<Integer, Layout>();

        reload();
    }

    private Layout(BufferedImage layout, Layout parent) {

        this.layout = layout;
        this.parent = parent;
    }

    public void reload() {

        if (parent != null) {
            parent.reload();
            return;
        }

        config = null;

        try {
            InputStream stream = getClass().getResourceAsStream(path + ".png");
            layout = ImageIO.read(stream);
            stream.close();
            try {
                config = new LayoutConfiguration(layout, path + ".layout");
            } catch (Exception ex) {
                config = null;
                layout = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                ex.printStackTrace();
            }
        } catch (Exception e) {
            layout = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            config = null;
            e.printStackTrace();
        }

        try {
            if (config == null)
                config = new LayoutConfiguration(layout);
        } catch (Exception e) {
            layout = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            e.printStackTrace();
        }

        colorMaps.clear();
        simplificationMaps.clear();
    }

    public BufferedImage getLayout(int color) {

        if (parent == null)
            return getSubLayout(0).getLayout(color);

        for (Entry<Integer, BufferedImage> e : colorMaps.entrySet())
            if (e.getKey().intValue() == color)
                return e.getValue();

        // Create a new image and get its graphics instance so we can draw on it
        BufferedImage img = new BufferedImage(getConfig().getWidth(), getConfig().getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // Fill BG
        g.setColor(new Color(0x000000));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        // Copy over the pixels that match the color we want
        g.setColor(new Color(0xFFFFFF));
        for (int x = 0; x < img.getWidth(); x++)
            for (int y = 0; y < img.getWidth(); y++)
                if ((layout.getRGB(x, y) & 0xFFFFFF) == color)
                    g.fillRect(x, y, 1, 1);

        // Dispose the graphics instance, we don't need it anymore
        g.dispose();

        // Store the image to the map so it can be used later
        colorMaps.put(color, img);

        return img;
    }

    public SimplifiedLayout getSimplifiedLayout(int color) {

        if (parent == null)
            return getSubLayout(0).getSimplifiedLayout(color);

        for (Entry<Integer, SimplifiedLayout> e : simplificationMaps.entrySet())
            if (e.getKey().intValue() == color)
                return e.getValue();

        SimplifiedLayout l = new SimplifiedLayout(this, color);
        simplificationMaps.put(color, l);
        return l;
    }

    public LayoutConfiguration getConfig() {

        if (parent != null)
            return parent.getConfig();

        return config;
    }

    public Layout getSubLayout(int id) {

        if (parent != null)
            return parent.getSubLayout(id);

        for (Entry<Integer, Layout> e : subLayouts.entrySet())
            if (e.getKey().intValue() == id)
                return e.getValue();

        BufferedImage img = getConfig().getSubLayout(layout, id);
        Layout layout = new Layout(img, this);
        subLayouts.put(id, layout);
        return layout;
    }

}
