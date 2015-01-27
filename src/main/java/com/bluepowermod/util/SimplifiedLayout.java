package com.bluepowermod.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SimplifiedLayout {

    private Layout layout;
    private int color;

    private List<Rectangle> rect = new ArrayList<Rectangle>();

    protected SimplifiedLayout(Layout layout, int color) {

        this.layout = layout;
        this.color = color;

        simplify();
    }

    private void simplify() {

        BufferedImage img = layout.getLayout(color);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                boolean found = false;
                for (Rectangle r : rect) {
                    if (r.contains(x + 0.5, y + 0.5)) {
                        found = true;
                        break;
                    }
                }
                // Already contained, we don't care about this one :P
                if (found)
                    continue;

                int rgb = img.getRGB(x, y) & 0xFFFFFF;

                if (rgb == 0x1a1a1a || rgb == 0)
                    continue;

                int x_ = x;
                for (; x_ < img.getWidth(); x_++) {
                    int rgb_ = img.getRGB(x_, y) & 0xFFFFFF;
                    if (rgb_ != rgb)
                        break;
                    boolean problem = false;
                    for (Rectangle r : rect) {
                        if (r.contains(x_ + 0.5, y + 0.5)) {
                            problem = true;
                            break;
                        }
                    }
                    if (problem)
                        break;
                }

                if (x_ == x)
                    continue;

                int y_ = y;
                for (; y_ < img.getHeight(); y_++) {
                    boolean problem = false;
                    for (int x__ = x; x__ < x_; x__++) {
                        int rgb_ = img.getRGB(x__, y_) & 0xFFFFFF;
                        if (rgb_ != rgb) {
                            problem = true;
                            break;
                        }
                        for (Rectangle r : rect) {
                            if (r.contains(x__ + 0.5, y_ + 0.5)) {
                                problem = true;
                                break;
                            }
                        }
                    }
                    if (problem)
                        break;
                }

                if (y_ == y)
                    continue;

                rect.add(new Rectangle(x, y, x_ - x, y_ - y));

                x = x_ - 1;
            }
        }
    }

    public int getColor() {

        return color;
    }

    public Layout getLayout() {

        return layout;
    }

    public List<Rectangle> getRectangles() {

        return rect;
    }

}
