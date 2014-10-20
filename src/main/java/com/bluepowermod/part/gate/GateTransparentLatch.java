package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Refs;

/**
 * @author MineMaarten
 */
public class GateTransparentLatch extends GateBase {

    private boolean mirrored;
    private IIcon mirroredTopIcon;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable();
        right().enable().setOutputOnly();
    }

    @Override
    public String getId() {

        return "transparent";
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        super.registerIcons(reg);
        mirroredTopIcon = reg.registerIcon(Refs.MODID + ":gates/" + getId() + "/base_mirrored");
    }

    @Override
    public IIcon getTopIcon() {
        return mirrored ? mirroredTopIcon : super.getTopIcon();
    }

    @Override
    public void renderTop(float frame) {

        if (mirrored) {
            GL11.glPushMatrix();
            GL11.glTranslated(0.5, 0, 0.5);
            GL11.glScaled(-1, 1, 1);
            GL11.glTranslated(-0.5, 0, -0.5);

            GL11.glDisable(GL11.GL_CULL_FACE);
        }

        renderTop("front", front());
        renderTop("left", mirrored ? right() : left());
        renderTop("back", back());
        renderTop("leftcenter", back().getInput() == 0);
        RenderHelper.renderRedstoneTorch(4 / 16D, 3D / 16D, -4 / 16D, 8D / 16D, back().getInput() == 0);
        RenderHelper.renderRedstoneTorch(4 / 16D, 3D / 16D, 1 / 16D, 8D / 16D, back().getInput() > 0 && front().getInput() == 0);
        RenderHelper.renderRedstoneTorch(1 / 16D, 3D / 16D, 1 / 16D, 8D / 16D, back().getInput() == 0 && front().getInput() == 0);
        RenderHelper.renderRedstoneTorch(-4 / 16D, 2D / 16D, 1 / 16D, 10D / 16D, front().getInput() > 0);
        RenderHelper.renderRedstoneTorch(2 / 16D, 2D / 16D, 6 / 16D, 10D / 16D, front().getInput() > 0);
        renderTop("right", mirrored ? left() : right());

        if (mirrored) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void doLogic() {

        if (back().getInput() > 0) {
            front().setOutput(mirrored ? right().getInput() : left().getInput());
            if (mirrored) {
                left().setOutput(right().getInput());
            } else {
                right().setOutput(left().getInput());
            }
        }
    }

    @Override
    protected boolean changeMode() {
        mirrored = !mirrored;
        if (mirrored) {
            left().setOutputOnly();
            right().setBidirectional();
        } else {
            left().setBidirectional();
            right().setOutputOnly();
        }
        sendUpdatePacket();
        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        writeUpdateToNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setBoolean("mirrored", mirrored);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        readUpdateFromNBT(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        mirrored = tag.getBoolean("mirrored");
        if (mirrored) {
            left().setOutputOnly();
            right().setBidirectional();
        } else {
            left().setBidirectional();
            right().setOutputOnly();
        }
        if (getWorld() != null && getWorld().isRemote)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    @Override
    public void addWailaInfo(List<String> info) {

    }

}