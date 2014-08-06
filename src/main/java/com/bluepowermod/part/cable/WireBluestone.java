package com.bluepowermod.part.cable;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.util.ForgeDirectionUtils;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.api.vec.Vector3Cube;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Refs;

public class WireBluestone extends CableWall {

    protected static Vector3Cube SELECTION_BOX = new Vector3Cube(0, 0, 0, 1, 1 / 16D, 1);
    protected static Vector3Cube OCCLUSION_BOX = new Vector3Cube(1 / 8D, 0, 1 / 8D, 15 / 16D, 1 / 8D, 7 / 8D);

    private static ResourceLocation textureOn = new ResourceLocation(Refs.MODID + ":textures/base/bluestoneOn.png");
    private static ResourceLocation textureOff = new ResourceLocation(Refs.MODID + ":textures/base/bluestoneOff.png");

    private boolean propagating = false;
    private int signal = 15;
    private int signalSelf = 0;

    @Override
    public String getType() {

        return "bluestoneWire";
    }

    @Override
    public String getUnlocalizedName() {

        return "bluestoneWire";
    }

    @Override
    public int getRotation() {

        return 0;
    }

    @Override
    public boolean canConnectToCable(CableWall cable) {

        return cable instanceof WireBluestone;
    }

    @Override
    public boolean canConnectToBlock(Block block, Vector3 location) {

        try {
            return block.canConnectRedstone(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                    ForgeDirectionUtils.getSide(loc.getDirectionTo(location)));
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public boolean canConnectToTileEntity(TileEntity tile) {

        return false;
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add(SELECTION_BOX.clone().toAABB());
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add(OCCLUSION_BOX.clone().toAABB());
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return CustomTabs.tabBluePowerCircuits;
    }

    @Override
    public boolean renderStatic(Vector3 loc, int pass) {

        GL11.glPushMatrix();
        {
            rotateAndTranslateDynamic(loc, pass, 0);

            if (signal > 0)
                Minecraft.getMinecraft().renderEngine.bindTexture(textureOn);
            else
                Minecraft.getMinecraft().renderEngine.bindTexture(textureOff);

            // Render center
            renderBox(7, 7, 9, 9);

            int[] sides = new int[] { 0, 0, 0, 0 };

            ForgeDirection f = ForgeDirection.getOrientation(getFace());
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (d == f || d == f.getOpposite())
                    continue;

                Vector3 v = (Vector3) connections[ForgeDirectionUtils.getSide(d)];
                if (v == null)
                    continue;

                int val = 1 + (v.distanceTo(this.loc) > 1 ? 1 : 0);

                switch (f) {
                case UP:
                    switch (d) {
                    case EAST:
                        sides[3] = val;
                        break;
                    case WEST:
                        sides[2] = val;
                        break;
                    case NORTH:
                        sides[0] = val;
                        break;
                    case SOUTH:
                        sides[1] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case DOWN:
                    switch (d) {
                    case EAST:
                        sides[3] = val;
                        break;
                    case WEST:
                        sides[2] = val;
                        break;
                    case NORTH:
                        sides[1] = val;
                        break;
                    case SOUTH:
                        sides[0] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case EAST:
                    switch (d) {
                    case UP:
                        sides[3] = val;
                        break;
                    case DOWN:
                        sides[2] = val;
                        break;
                    case NORTH:
                        sides[0] = val;
                        break;
                    case SOUTH:
                        sides[1] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case WEST:
                    switch (d) {
                    case UP:
                        sides[2] = val;
                        break;
                    case DOWN:
                        sides[3] = val;
                        break;
                    case NORTH:
                        sides[0] = val;
                        break;
                    case SOUTH:
                        sides[1] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case NORTH:
                    switch (d) {
                    case UP:
                        sides[0] = val;
                        break;
                    case DOWN:
                        sides[1] = val;
                        break;
                    case EAST:
                        sides[3] = val;
                        break;
                    case WEST:
                        sides[2] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case SOUTH:
                    switch (d) {
                    case UP:
                        sides[1] = val;
                        break;
                    case DOWN:
                        sides[0] = val;
                        break;
                    case EAST:
                        sides[2] = val;
                        break;
                    case WEST:
                        sides[3] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                default:
                    break;
                }
            }

            if (sides[3] > 0)// East
                renderBox(0 - (sides[3] - 1), 7, 7, 9);
            if (sides[2] > 0)// West
                renderBox(9, 7, 16 + (sides[2] - 1), 9);
            if (sides[1] > 0)// South
                renderBox(7, 0 - (sides[1] - 1), 9, 7);
            if (sides[0] > 0)// North
                renderBox(7, 9, 9, 16 + (sides[0] - 1));
        }
        GL11.glPopMatrix();

        return true;
    }

    private void renderBox(int minx, int minz, int maxx, int maxz) {

        GL11.glBegin(GL11.GL_QUADS);
        {
            // Top
            GL11.glNormal3d(0, 1, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, 1 / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, 1 / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 1 / 16D, maxz / 16D, maxx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 1 / 16D, minz / 16D, maxx / 16D, minz / 16D);
            // Bottom
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, 0, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 0, minz / 16D, maxx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 0, maxz / 16D, maxx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, 0, maxz / 16D, minx / 16D, maxz / 16D);
            // South
            GL11.glNormal3d(1, 0, 0);
            RenderHelper.addVertexWithTexture(maxx / 16D, 1 / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 1 / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 0, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 0, minz / 16D, minx / 16D, minz / 16D);
            // North
            GL11.glNormal3d(-1, 0, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, 1 / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, 0, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, 0, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, 1 / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            // East
            GL11.glNormal3d(0, 1, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, 0, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, 1 / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 1 / 16D, minz / 16D, maxx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 0, minz / 16D, maxx / 16D, minz / 16D);
            // West
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, 0, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 0, maxz / 16D, maxx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, 1 / 16D, maxz / 16D, maxx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, 1 / 16D, maxz / 16D, minx / 16D, maxz / 16D);
        }
        GL11.glEnd();
    }

    public void propagate(WireBluestone from) {

        if (propagating)
            return;

        propagating = true;

        // int amt = 0;
        // if (from != null)
        // amt = from.signal;
        // amt = Math.max(amt, signalSelf);
        //
        // for (Object o : connections) {
        // if (o == from)
        // continue;
        // if (o instanceof WireBluestone) {
        // ((WireBluestone) o).propagate(this);
        // }
        // }

        propagating = false;
    }

    @Override
    public void update() {

        super.update();

        // int oldSS = signalSelf;
        //
        // signalSelf = 0;
        // for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
        // if (d == ForgeDirection.getOrientation(getFace()) || d == ForgeDirection.getOrientation(getFace()).getOpposite())
        // continue;
        // signalSelf = Math.max(signalSelf,
        // RedstoneHelper.getInput(getWorld(), getX(), getY(), getZ(), d, ForgeDirection.getOrientation(getFace())));
        // }
        // if (signalSelf != oldSS) {
        // propagate(null);
        // }
    }

    @Override
    public void writeUpdatePacket(NBTTagCompound tag) {

        super.writeUpdatePacket(tag);

        tag.setInteger("signal", signal);
    }

    @Override
    public void readUpdatePacket(NBTTagCompound tag) {

        super.readUpdatePacket(tag);

        signal = tag.getInteger("signal");
    }

}
