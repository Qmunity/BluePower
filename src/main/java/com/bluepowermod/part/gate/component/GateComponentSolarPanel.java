package com.bluepowermod.part.gate.component;

import java.awt.image.BufferedImage;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.gate.GateBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateComponentSolarPanel extends GateComponent {

    private boolean state = false;

    private int layoutColor = -1;
    private double x = 0, z = 0;

    public GateComponentSolarPanel(GateBase gate, int color) {

        super(gate);

        layoutColor = color;
        onLayoutRefresh();
    }

    public GateComponentSolarPanel(GateBase gate, double x, double z) {

        super(gate);

        this.x = x;
        this.z = z;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        renderer.addTransformation(new Translation(-3 / 16D + x, 0, -4 / 16D + z));
        renderer.renderBox(new Vec3dCube(3 / 16D, 2 / 16D, 4 / 16D, 13 / 16D, 4 / 16D, 12 / 16D), null, IconSupplier.gateSolarPanel,
                IconSupplier.gateButton, IconSupplier.gateButton, IconSupplier.gateButton, IconSupplier.gateButton);
        renderer.removeTransformation();
    }

    @Override
    public void onLayoutRefresh() {

        if (layoutColor == -1)
            return;

        BufferedImage img = getGate().getLayout().getLayout(layoutColor);
        x = img.getWidth();
        z = img.getHeight();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getWidth(); y++) {
                if ((img.getRGB(x, y) & 0xFFFFFF) != 0) {
                    this.x = Math.min(this.x, x);
                    z = Math.min(z, y);
                }
            }
        }
        x = x / (img.getWidth());
        z = z / (img.getHeight());
    }

    public void setState(boolean state) {

        this.state = state;
    }

    public boolean getState() {

        return state;
    }

}
