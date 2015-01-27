package com.bluepowermod.part.gate.component;

import java.awt.Rectangle;

import net.minecraft.util.IIcon;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Scale;
import uk.co.qmunity.lib.vec.Vec2dRect;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.part.gate.GateBase;

public class GateComponentBorder extends GateComponentLocationArray {

    public GateComponentBorder(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate, color);
    }

    @Override
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        double height = 1 / 64D;
        IIcon texture = getGate().getDarkTop();
        double scale = 1D / getGate().getLayout().getLayout(layoutColor).getWidth();

        renderer.setRenderSides(false, true, true, true, true, true);
        for (Rectangle r : getGate().getLayout().getSimplifiedLayout(layoutColor).getRectangles())
            renderer.renderBox(
                    new Vec2dRect(r).extrude(height).transform(new Scale(scale, 1, scale)).add(-0.5 + 1 / 64D, 2 / 16D, -0.5 + 1 / 64D),
                    texture);

        // for (int x = 0; x < pixels.length; x++) {
        // boolean[] p = pixels[x];
        // for (int y = 0; y < p.length; y++) {
        // if (p[y]) {
        // double dx = x / (double) pixels.length;
        // double dy = y / (double) p.length;
        //
        // boolean west = x == 0 || !pixels[x - 1][y];
        // boolean east = x == pixels.length - 1 || !pixels[x + 1][y];
        // boolean north = y == 0 || !pixels[x][y - 1];
        // boolean south = y == p.length - 1 || !pixels[x][y + 1];
        //
        // renderer.setRenderSides(false, true, west, east, north, south);
        // renderer.renderBox(new Vec3dCube(dx, 2 / 16D, dy, dx + size, 2 / 16D + height, dy + size), texture);
        // }
        // }
        // }
        renderer.resetRenderedSides();
    }

}
