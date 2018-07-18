/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container.stack;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.render.RenderHelper;
import com.bluepowermod.reference.Refs;

/**
 *
 * @author MineMaarten
 */

public class TubeStack {

    public ItemStack stack;
    public final TubeColor color;
    public double progress; // 0 at the start, 0.5 on an intersection, 1 at the end.
    public double oldProgress;
    public EnumFacing heading;
    public boolean enabled = true; // will be disabled when the client sided stack is at an intersection, at which point it needs to wait for server
    // input. This just serves a visual purpose.
    public int idleCounter; // increased when the stack is standing still. This will cause the client to remove the stack when a timeout occurs.
    private TileEntity target; // only should have a value when retrieving items. this is the target the item wants to go to.
    private int targetX, targetY, targetZ;
    public static final double ITEM_SPEED = 0.0625;
    private double speed = ITEM_SPEED;
    public static double tickTimeMultiplier = 1;//Used client side to correct for TPS lag. This is being synchronized from the server.

    @SideOnly(Side.CLIENT)
    private static RenderItem customRenderItem;
    private static EntityItem renderedItem;

    public static RenderMode renderMode;

    public static enum RenderMode {
        AUTO, NORMAL, REDUCED, NONE
    }

    public TubeStack(ItemStack stack, EnumFacing from) {

        this(stack, from, TubeColor.NONE);
    }

    public TubeStack(ItemStack stack, EnumFacing from, TubeColor color) {

        heading = from;
        this.stack = stack;
        this.color = color;
    }

    public void setSpeed(double speed) {

        this.speed = speed;
    }

    public double getSpeed() {

        return speed;
    }

    /**
     * Updates the movement by the given m/tick.
     * @return true if the stack has gone past the center, meaning logic needs to be triggered.
     */
    public boolean update(World worldObj) {

        oldProgress = progress;
        if (enabled) {
            boolean isEntering = progress < 0.5;
            progress += speed * (worldObj.isRemote ? tickTimeMultiplier : 1);
            return progress >= 0.5 && isEntering;
        } else {
            idleCounter++;
            return false;
        }
    }

    public TileEntity getTarget(World world) {

        if (target == null && (targetX != 0 || targetY != 0 || targetZ != 0)) {
            target = world.getTileEntity(new BlockPos(targetX, targetY, targetZ));
        }
        return target;
    }


    public void setTarget(TileEntity tileEntity) {
        target = tileEntity;
        if (target != null) {
            targetX = target.getPos().getX();
            targetY = target.getPos().getY();
            targetZ = target.getPos().getZ();
        } else {
            targetX = 0;
            targetY = 0;
            targetZ = 0;
        }
    }

    public TubeStack copy() {

        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return loadFromNBT(tag);
    }

    public void writeToNBT(NBTTagCompound tag) {

        stack.writeToNBT(tag);
        tag.setByte("color", (byte) color.ordinal());
        tag.setByte("heading", (byte) heading.ordinal());
        tag.setDouble("progress", progress);
        tag.setDouble("speed", speed);
        tag.setInteger("targetX", targetX);
        tag.setInteger("targetY", targetY);
        tag.setInteger("targetZ", targetZ);
    }

    public static TubeStack loadFromNBT(NBTTagCompound tag) {

        TubeStack stack = new TubeStack(new ItemStack(tag), EnumFacing.byIndex(tag.getByte("heading")),
                TubeColor.values()[tag.getByte("color")]);
        stack.progress = tag.getDouble("progress");
        stack.speed = tag.getDouble("speed");
        stack.targetX = tag.getInteger("targetX");
        stack.targetY = tag.getInteger("targetY");
        stack.targetZ = tag.getInteger("targetZ");
        return stack;
    }

    public void writeToPacket(ByteBuf buf) {

        ByteBufUtils.writeItemStack(buf, stack);
        buf.writeByte(heading.ordinal());
        buf.writeByte((byte) color.ordinal());
        buf.writeDouble(speed);
        buf.writeDouble(progress);
    }

    public static TubeStack loadFromPacket(ByteBuf buf) {

        TubeStack stack = new TubeStack(ByteBufUtils.readItemStack(buf), EnumFacing.byIndex(buf.readByte()),
                TubeColor.values()[buf.readByte()]);
        stack.speed = buf.readDouble();
        stack.progress = buf.readDouble();
        return stack;
    }

    @SideOnly(Side.CLIENT)
    public void render(float partialTick) {

        if (renderMode == RenderMode.AUTO) {
            renderMode = Minecraft.getMinecraft().gameSettings.fancyGraphics ? RenderMode.NORMAL : RenderMode.REDUCED;
        }
        final RenderMode finalRenderMode = renderMode;

        if (customRenderItem == null) {
            customRenderItem = Minecraft.getMinecraft().getRenderItem();

            renderedItem = new EntityItem(FMLClientHandler.instance().getWorldClient());
            renderedItem.hoverStart = 0.0F;
        }

        double renderProgress = (oldProgress + (progress - oldProgress) * partialTick) * 2 - 1;

        GL11.glPushMatrix();
        GL11.glTranslated(heading.getXOffset() * renderProgress * 0.5, heading.getYOffset() * renderProgress * 0.5, heading.getZOffset() * renderProgress * 0.5);
        if (finalRenderMode != RenderMode.NONE) {
            GL11.glPushMatrix();
            if (stack.getCount() > 5) {
                GL11.glScaled(0.8, 0.8, 0.8);
            }
            if (!(stack.getItem() instanceof ItemBlock)) {
                GL11.glScaled(0.8, 0.8, 0.8);
                GL11.glTranslated(0, -0.15, 0);
            }

            customRenderItem.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
            GL11.glPopMatrix();
        } else {
            float size = 0.02F;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_QUADS);
            RenderHelper.drawColoredCube(new AxisAlignedBB(-size, -size, -size, size, size, size), 1, 1, 1, 1);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        if (color != TubeColor.NONE) {

            float size = 0.2F;

            int colorInt = ItemDye.DYE_COLORS[color.ordinal()];
            float red = (colorInt >> 16) / 256F;
            float green = (colorInt >> 8 & 255) / 256F;
            float blue = (colorInt & 255) / 256F;

            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor3f(red, green, blue);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID, "textures/blocks/tubes/inside_color_border.png"));
            RenderHelper.drawTesselatedTexturedCube(new AxisAlignedBB(-size, -size, -size, size, size, size));
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glPopMatrix();
    }
}