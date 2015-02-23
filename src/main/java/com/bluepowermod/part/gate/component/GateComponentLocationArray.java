package com.bluepowermod.part.gate.component;

import java.awt.image.BufferedImage;

import com.bluepowermod.part.gate.GateBase;

public abstract class GateComponentLocationArray extends GateComponent {

    protected int layoutColor = -1;

    protected boolean[][] pixels;

    public GateComponentLocationArray(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate);

        layoutColor = color;
        onLayoutRefresh();
    }

    @Override
    public void onLayoutRefresh() {

        if (layoutColor == -1)
            return;

        BufferedImage img = getGate().getLayout().getLayout(layoutColor);
        pixels = new boolean[img.getWidth()][img.getHeight()];
        for (int x = 0; x < pixels.length; x++) {
            boolean[] p = pixels[x];
            for (int y = 0; y < p.length; y++) {
                if ((img.getRGB(x, y) & 0xFFFFFF) != 0)
                    p[y] = true;
            }
        }
    }

}
