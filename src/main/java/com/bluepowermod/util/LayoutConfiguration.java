package com.bluepowermod.util;

import java.awt.image.BufferedImage;
import java.io.InputStreamReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class LayoutConfiguration {

    private int width, height;
    private int layouts = 1;

    protected LayoutConfiguration(BufferedImage img) throws Exception {

        if (img == null)
            throw new Exception("You need a valid image in order to create a layout configuration.");
        if (img.getWidth() != img.getHeight())
            throw new Exception("The layout isn't symmetrical and a .layout file isn't provided. Loading an empty layout!");

        width = img.getWidth();
        height = img.getHeight();
    }

    protected LayoutConfiguration(BufferedImage img, String file) throws Exception {

        if (img == null)
            throw new Exception("You need a valid image in order to create a layout configuration.");

        width = img.getWidth();
        height = img.getHeight();

        JsonObject obj = null;

        try {
            obj = (JsonObject) new JsonParser().parse(new JsonReader(new InputStreamReader(getClass().getResourceAsStream(file))));
        } catch (Exception ex) {
            if (img.getWidth() != img.getHeight())
                throw new Exception("The layout isn't symmetrical and a .layout file isn't provided. Loading an empty layout!");
            return;
        }

        if (obj.has("width") || obj.has("height")) {
            if (obj.has("width") != obj.has("height"))
                throw new Exception(
                        "One of the layout size parameters hasn't been set. You need either none or both of them. Loading an empty layout!");
            width = obj.getAsJsonPrimitive("width").getAsInt();
            height = obj.getAsJsonPrimitive("height").getAsInt();
        }
        if (obj.has("layouts")) {
            layouts = obj.getAsJsonPrimitive("layouts").getAsInt();
            if (layouts <= 0)
                throw new Exception("You must have one or more layouts in the same file. Loading an empty layout!");
            if (layouts * height > img.getHeight())
                throw new Exception("The height of an image of size <layouts> * <layout_height> (" + (layouts * height)
                        + ") exceeds the layout image height (" + img.getHeight() + "). Loading an empty layout!");
        }
    }

    public int getWidth() {

        return width;
    }

    public int getHeight() {

        return height;
    }

    public int getLayouts() {

        return layouts;
    }

    public BufferedImage getSubLayout(BufferedImage img, int id) {

        if (id >= layouts)
            return null;

        if (img.getWidth() != getWidth() || img.getHeight() % getHeight() != 0)
            return null;

        return img.getSubimage(0, id * getHeight(), getWidth(), getHeight());
    }

}
