package com.bluepowermod.part.gate.component;

import java.awt.image.BufferedImage;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.gate.GateBase;

public class GateComponentTorch extends GateComponent {

    private final Random rnd = new Random();

    private boolean state = false;

    private int layoutColor = -1;
    protected double x = 0, z = 0;
    protected double height;

    private boolean digital;

    public GateComponentTorch(GateBase gate, int color, double height, boolean digital) {

        super(gate);

        layoutColor = color;
        onLayoutRefresh();

        this.height = height;

        this.digital = digital;
    }

    public GateComponentTorch(GateBase gate, double x, double z, double height, boolean digital) {

        super(gate);

        this.x = x;
        this.z = z;
        this.height = height;

        this.digital = digital;
    }

    @Override
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        IIcon icon = digital ? (state ? IconSupplier.bluestoneTorchOn : IconSupplier.bluestoneTorchOff) : (state ? Blocks.redstone_torch
                .getIcon(0, 0) : Blocks.unlit_redstone_torch.getIcon(0, 0));

        double height = 10 / 16D - this.height;

        renderer.addTransformation(new Translation(x - 7 / 16D, 2 / 16D - height, z - 7 / 16D));

        renderer.setRenderSides(false, false, false, false, true, true);
        renderer.renderBox(new Vec3dCube(6 / 16D, height, 7 / 16D, 10 / 16D, 11 / 16D, 9 / 16D), icon);

        renderer.setRenderSides(false, false, true, true, false, false);
        renderer.renderBox(new Vec3dCube(7 / 16D, height, 6 / 16D, 9 / 16D, 11 / 16D, 10 / 16D), icon);

        renderer.addTransformation(new Translation(0, 0, 1 / 16D));

        renderer.setRenderSides(false, true, false, false, false, false);
        renderer.renderBox(new Vec3dCube(7 / 16D, 10 / 16D, 6 / 16D, 9 / 16D, 10 / 16D, 8 / 16D), icon);

        renderer.removeTransformation();

        renderer.resetRenderedSides();

        renderer.removeTransformation();
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

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

    @Override
    public void tick() {

        super.tick();

        if (!state)
            return;

        GateBase gate = getGate();

        if (!gate.getWorld().isRemote)
            return;

        Vec3d v = new Vec3d(x + 1 / 16D, height + 2 / 16D, z + 1 / 16D).sub(Vec3d.center).rotate(0, 90 * gate.getRotation(), 0)
                .add(Vec3d.center).rotate(gate.getFace(), Vec3d.center);
        if (rnd.nextInt(10) == 0)
            gate.getWorld().spawnParticle("reddust", gate.getX() + v.getX(), gate.getY() + v.getY(), gate.getZ() + v.getZ(),
                    digital ? -1 : 0, 0, digital ? 1 : 0);
    }

    public void setState(boolean state) {

        this.state = state;
    }

    public boolean getState() {

        return state;
    }

}
