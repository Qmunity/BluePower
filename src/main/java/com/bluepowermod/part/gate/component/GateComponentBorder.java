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

        renderer.resetRenderedSides();
    }

}
