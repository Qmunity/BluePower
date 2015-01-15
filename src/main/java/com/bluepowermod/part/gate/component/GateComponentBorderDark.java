package com.bluepowermod.part.gate.component;

import net.minecraft.util.IIcon;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.part.gate.GateBase;

public class GateComponentBorderDark extends GateComponentLocationArray {

    public GateComponentBorderDark(GateBase gate, int color) {

        super(gate, color);
    }

    @Override
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        double height = 1 / 48D;
        double size = 1 / ((double) pixels.length);
        IIcon texture = getGate().getDarkTop();

        for (int x = 0; x < pixels.length; x++) {
            boolean[] p = pixels[x];
            for (int y = 0; y < p.length; y++) {
                if (p[y]) {
                    double dx = x / (double) pixels.length;
                    double dy = y / (double) p.length;

                    boolean west = x == 0 || !pixels[x - 1][y];
                    boolean east = x == pixels.length - 1 || !pixels[x + 1][y];
                    boolean north = y == 0 || !pixels[x][y - 1];
                    boolean south = y == p.length - 1 || !pixels[x][y + 1];

                    renderer.setRenderSides(false, true, west, east, north, south);
                    renderer.setColor(0x555555);
                    renderer.renderBox(new Vec3dCube(dx, 2 / 16D, dy, dx + size, 2 / 16D + height, dy + size), texture);
                    renderer.setColor(0xFFFFFF);
                }
            }
        }
        renderer.resetRenderedSides();
    }

}
