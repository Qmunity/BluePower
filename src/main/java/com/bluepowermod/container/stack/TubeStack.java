/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container.stack;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.render.RenderHelper;

import net.minecraft.world.item.ItemStack;

/**
 *
 * @author MineMaarten
 */

public class TubeStack implements IItemHandler {

    public ItemStack stack;
    public TubeColor color;
    public double progress; // 0 at the start, 0.5 on an intersection, 1 at the end.
    public double oldProgress;
    public final Direction heading;
    public boolean enabled = true; // will be disabled when the client sided stack is at an intersection, at which point it needs to wait for server
    // input. This just serves a visual purpose.
    public int idleCounter; // increased when the stack is standing still. This will cause the client to remove the stack when a timeout occurs.
    private BlockEntity target; // only should have a value when retrieving items. this is the target the item wants to go to.
    private int targetX, targetY, targetZ;
    public static final double ITEM_SPEED = 0.0625;
    private double speed = ITEM_SPEED;
    public static double tickTimeMultiplier = 1;//Used client side to correct for TPS lag. This is being synchronized from the server.

    @OnlyIn(Dist.CLIENT)
    private static ItemRenderer customRenderItem;
    private static ItemEntity renderedItem;

    public static RenderMode renderMode;

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return stack;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack remainder = stack.copy();
        if(this.stack.isEmpty()) {
            remainder = ItemStack.EMPTY;
            if(!simulate) {
                this.stack = stack.copy();
            }
        }else if(this.stack.getItem().equals(stack.getItem()) && this.stack.getCount() < this.stack.getMaxStackSize()){
            int amount = Math.min(stack.getCount(), this.stack.getMaxStackSize() - this.stack.getCount());
            remainder.shrink(amount);
            if(!simulate) {
                this.stack.grow(amount);
            }
        }
        return remainder;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack ret = this.stack.copy();
        if(!this.stack.isEmpty()){
            if(this.stack.getCount() > amount){
                ret.setCount(amount);
                if(!simulate){
                    this.stack.shrink(amount);
                }
            }else{
                ret = ItemStack.EMPTY;
                if(!simulate){
                    this.stack = ItemStack.EMPTY;
                }
            }
        }
        return ret;
    }

    @Override
    public int getSlotLimit(int slot) {
        return stack.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.stack.isEmpty() || (this.stack.getItem() == stack.getItem() && this.stack.getCount() < this.stack.getMaxStackSize());
    }

    public static enum RenderMode {
        AUTO, NORMAL, REDUCED, NONE
    }

    public TubeStack(ItemStack stack, Direction from) {

        this(stack, from, TubeColor.NONE);
    }

    public TubeStack(ItemStack stack, Direction from, TubeColor color) {
        this.heading = from;
        this.progress = 0;
        this.speed = 1;
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
    public boolean update(Level worldObj) {

        oldProgress = progress;
        if (enabled) {
            boolean isEntering = progress < 0.5;
            progress += speed * (worldObj.isClientSide ? tickTimeMultiplier : 1);
            return progress >= 0.5 && isEntering;
        } else {
            idleCounter++;
            return false;
        }
    }

    public BlockEntity getTarget(Level world) {

        if (target == null && (targetX != 0 || targetY != 0 || targetZ != 0)) {
            target = world.getBlockEntity(new BlockPos(targetX, targetY, targetZ));
        }
        return target;
    }


    public void setTarget(BlockEntity tileEntity) {
        target = tileEntity;
        if (target != null) {
            targetX = target.getBlockPos().getX();
            targetY = target.getBlockPos().getY();
            targetZ = target.getBlockPos().getZ();
        } else {
            targetX = 0;
            targetY = 0;
            targetZ = 0;
        }
    }

    public TubeStack copy(HolderLookup.Provider provider) {

        // Create output
        TagValueOutput output = TagValueOutput.createWithContext(
                ProblemReporter.DISCARDING,
                provider
        );

        // Write to output
        writeToOutput(output);

        // Build tag
        CompoundTag tag = output.buildResult();

        // Create input
        ValueInput input = TagValueInput.create(
                ProblemReporter.DISCARDING,
                provider,
                tag
        );

        // Load from input
        return loadFromInput(input);
    }

    public void writeToOutput(ValueOutput output) {
        output.store("stack", ItemStack.CODEC, stack);
        output.putByte("color", (byte) color.ordinal());
        output.putByte("heading", (byte) heading.ordinal());
        output.putDouble("progress", progress);
        output.putDouble("speed", speed);
        output.putInt("targetX", targetX);
        output.putInt("targetY", targetY);
        output.putInt("targetZ", targetZ);
    }

    public static TubeStack loadFromInput(ValueInput input) {
        TubeStack stack = new TubeStack(input.read("stack", ItemStack.CODEC).get(), Direction.from3DDataValue(input.getByteOr("heading", (byte)0)),
                TubeColor.values()[input.getByteOr("color", (byte) TubeColor.NONE.ordinal())]);
        stack.progress = input.getDoubleOr("progress", 0);
        stack.speed = input.getDoubleOr("speed", 0);
        stack.targetX = input.getIntOr("targetX", 0);
        stack.targetY = input.getIntOr("targetY", 0);
        stack.targetZ = input.getIntOr("targetZ", 0);
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        if (renderMode == RenderMode.AUTO) {
            renderMode = Minecraft.getInstance().options.graphicsMode().get() == GraphicsStatus.FANCY ? RenderMode.NORMAL : RenderMode.REDUCED;
        }
        final RenderMode finalRenderMode = renderMode;

        if (customRenderItem == null) {
            customRenderItem = Minecraft.getInstance().getItemRenderer();

            renderedItem = new ItemEntity(Minecraft.getInstance().level, 0,0,0, ItemStack.EMPTY);
        }

        double renderProgress = (oldProgress + (progress - oldProgress) * partialTick) * 2 - 1;

        poseStack.pushPose();
        poseStack.translate(heading.getStepX() * renderProgress * 0.5, heading.getStepY() * renderProgress * 0.5, heading.getStepZ() * renderProgress * 0.5);
        if (finalRenderMode != RenderMode.NONE) {
            poseStack.pushPose();
            if (stack.getCount() > 5) {
                poseStack.scale(0.8F, 0.8F, 0.8F);
            }
            if (!(stack.getItem() instanceof BlockItem)) {
                poseStack.scale(0.8F, 0.8F, 0.8F);
                poseStack.translate(0, -0.15, 0);
            }

            customRenderItem.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, null, 0);

            poseStack.popPose();
        } else {
            float size = 0.02F;
            //TODO: RenderHelper.drawColoredCube(new AABB(-size, -size, -size, size, size, size), poseStack, 1, 1, 1, 1);
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

            //TODO: bind texture textures/blocks/tubes/inside_color_border.png

            RenderHelper.drawTesselatedTexturedCube(new AABB(-size, -size, -size, size, size, size));
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glPopMatrix();
    }
}