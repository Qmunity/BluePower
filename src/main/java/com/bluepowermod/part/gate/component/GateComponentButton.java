package com.bluepowermod.part.gate.component;

import java.awt.image.BufferedImage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.gate.GateBase;

public abstract class GateComponentButton extends GateComponent {

    private boolean state = false;

    private int layoutColor = -1;
    private double x = 0, z = 0;

    public GateComponentButton(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate);

        layoutColor = color;
        onLayoutRefresh();
    }

    public GateComponentButton(GateBase<?, ?, ?, ?, ?, ?> gate, double x, double z) {

        super(gate);

        this.x = x;
        this.z = z;
    }

    protected abstract IIcon getIcon();

    @Override
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        renderer.renderBox(new Vec3dCube(x, 2 / 16D, z, x + 4 / 16D, 3.5 / 16D, z + 4 / 16D), IconSupplier.gateButton);
        renderer.renderBox(new Vec3dCube(x + 1 / 32D, 3.5 / 16D, z + 1 / 32D, x + 4 / 16D - 1 / 32D, 4 / 16D, z + 4 / 16D - 1 / 32D),
                getIcon());
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

    public GateComponentButton setState(boolean state) {

        this.state = state;

        return this;
    }

    public boolean getState() {

        return state;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setBoolean("state", state);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        state = tag.getBoolean("state");
    }

}
