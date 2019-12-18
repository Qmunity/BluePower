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
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.lwjgl.opengl.GL11;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.render.RenderHelper;

/**
 *
 * @author MineMaarten
 */

public class TubeStack {

    public ItemStack stack;
    public final TubeColor color;
    public double progress; // 0 at the start, 0.5 on an intersection, 1 at the end.
    public double oldProgress;
    public Direction heading;
    public boolean enabled = true; // will be disabled when the client sided stack is at an intersection, at which point it needs to wait for server
    // input. This just serves a visual purpose.
    public int idleCounter; // increased when the stack is standing still. This will cause the client to remove the stack when a timeout occurs.
    private TileEntity target; // only should have a value when retrieving items. this is the target the item wants to go to.
    private int targetX, targetY, targetZ;
    public static final double ITEM_SPEED = 0.0625;
    private double speed = ITEM_SPEED;
    public static double tickTimeMultiplier = 1;//Used client side to correct for TPS lag. This is being synchronized from the server.

    @OnlyIn(Dist.CLIENT)
    private static ItemRenderer customRenderItem;
    private static ItemEntity renderedItem;

    public static RenderMode renderMode;

    public static enum RenderMode {
        AUTO, NORMAL, REDUCED, NONE
    }

    public TubeStack(ItemStack stack, Direction from) {

        this(stack, from, TubeColor.NONE);
    }

    public TubeStack(ItemStack stack, Direction from, TubeColor color) {

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

        CompoundNBT tag = new CompoundNBT();
        writeToNBT(tag);
        return loadFromNBT(tag);
    }

    public void writeToNBT(CompoundNBT tag) {

        stack.write(tag);
        tag.putByte("color", (byte) color.ordinal());
        tag.putByte("heading", (byte) heading.ordinal());
        tag.putDouble("progress", progress);
        tag.putDouble("speed", speed);
        tag.putInt("targetX", targetX);
        tag.putInt("targetY", targetY);
        tag.putInt("targetZ", targetZ);
    }

    public static TubeStack loadFromNBT(CompoundNBT tag) {

        TubeStack stack = new TubeStack(new ItemStack((IItemProvider) tag), Direction.byIndex(tag.getByte("heading")),
                TubeColor.values()[tag.getByte("color")]);
        stack.progress = tag.getDouble("progress");
        stack.speed = tag.getDouble("speed");
        stack.targetX = tag.getInt("targetX");
        stack.targetY = tag.getInt("targetY");
        stack.targetZ = tag.getInt("targetZ");
        return stack;
    }

    public void writeToPacket(ByteBuf buf) {
        //TODO: Add Items
        //ByteBufUtils.writeItemStack(buf, stack);
        buf.writeByte(heading.ordinal());
        buf.writeByte((byte) color.ordinal());
        buf.writeDouble(speed);
        buf.writeDouble(progress);
    }

    public static TubeStack loadFromPacket(ByteBuf buf) {
        //TODO: Add Items
        //ByteBufUtils.readItemStack(buf)
        TubeStack stack = new TubeStack(null, Direction.byIndex(buf.readByte()),
                TubeColor.values()[buf.readByte()]);
        stack.speed = buf.readDouble();
        stack.progress = buf.readDouble();
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(float partialTick) {

        if (renderMode == RenderMode.AUTO) {
            renderMode = Minecraft.getInstance().gameSettings.fancyGraphics ? RenderMode.NORMAL : RenderMode.REDUCED;
        }
        final RenderMode finalRenderMode = renderMode;

        if (customRenderItem == null) {
            customRenderItem = Minecraft.getInstance().getItemRenderer();

            renderedItem = new ItemEntity(Minecraft.getInstance().world, 0,0,0);
        }

        double renderProgress = (oldProgress + (progress - oldProgress) * partialTick) * 2 - 1;

        GL11.glPushMatrix();
        GL11.glTranslated(heading.getXOffset() * renderProgress * 0.5, heading.getYOffset() * renderProgress * 0.5, heading.getZOffset() * renderProgress * 0.5);
        if (finalRenderMode != RenderMode.NONE) {
            GL11.glPushMatrix();
            if (stack.getCount() > 5) {
                GL11.glScaled(0.8, 0.8, 0.8);
            }
            if (!(stack.getItem() instanceof BlockItem)) {
                GL11.glScaled(0.8, 0.8, 0.8);
                GL11.glTranslated(0, -0.15, 0);
            }

            //TODO: customRenderItem.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
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

            int colorInt = DyeColor.values()[color.ordinal()].getId();
            float red = (colorInt >> 16) / 256F;
            float green = (colorInt >> 8 & 255) / 256F;
            float blue = (colorInt & 255) / 256F;

            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor3f(red, green, blue);
            //TODO: Find replacement for RenderEngine
            //Minecraft.getInstance().renderEngine.bindTexture(new ResourceLocation(Refs.MODID, "textures/blocks/tubes/inside_color_border.png"));
            RenderHelper.drawTesselatedTexturedCube(new AxisAlignedBB(-size, -size, -size, size, size, size));
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glPopMatrix();
    }
}