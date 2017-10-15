/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.api.tube.ITubeConnection;
import com.bluepowermod.api.tube.IWeightedTubeInventory;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.TileEntityCache;
import com.bluepowermod.part.tube.TubeStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author MineMaarten
 */
public class TileMachineBase extends TileBase implements ITubeConnection, IWeightedTubeInventory, IEjectAnimator {

    protected boolean spawnItemsInWorld = true;
    protected boolean acceptsTubeItems = true;
    private final List<TubeStack> internalItemStackBuffer = new ArrayList<TubeStack>();
    private TileEntityCache tileCache;
    public static final int BUFFER_EMPTY_INTERVAL = 10;
    protected byte animationTicker = -1;
    protected static final int ANIMATION_TIME = 7;
    private boolean isAnimating;
    protected boolean ejectionScheduled;
    private static final int WARNING_INTERVAL = 600; // Every 30s

    @Override
    public void update() {

        super.update();

        if (!world.isRemote) {
            if (ejectionScheduled || getTicker() % BUFFER_EMPTY_INTERVAL == 0) {
                ejectItems();
                ejectionScheduled = false;
            }
        }
    }

    private void ejectItems() {

        for (Iterator<TubeStack> iterator = internalItemStackBuffer.iterator(); iterator.hasNext();) {
            TubeStack tubeStack = iterator.next();
            if (IOHelper.canInterfaceWith(getTileCache(getOutputDirection()), getFacingDirection())) {
                ItemStack returnedStack = IOHelper.insert(getTileCache(getOutputDirection()), tubeStack.stack, getFacingDirection(), tubeStack.color,
                        false);
                if (returnedStack.isEmpty()) {
                    iterator.remove();
                    markDirty();
                    if (!ejectionScheduled)
                        break;
                } else if (returnedStack.getCount() != tubeStack.stack.getCount()) {
                    markDirty();
                    if (!ejectionScheduled)
                        break;
                } else {
                    break;
                }
            } else if (spawnItemsInWorld) {
                EnumFacing direction = getFacingDirection().getOpposite();
                Block block = world.getBlockState(pos.offset(direction)).getBlock();
                if (block.isPassable(world, pos.offset(direction)) || block instanceof BlockLiquid || block instanceof IFluidBlock) {
                    ejectItemInWorld(tubeStack.stack, direction);
                    iterator.remove();
                    markDirty();
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public void validate() {

        super.validate();
        tileCache = null;
    }

    @Override
    public void onBlockNeighbourChanged() {

        super.onBlockNeighbourChanged();
        tileCache = null;
    }

    protected void addItemToOutputBuffer(ItemStack stack, TubeColor color) {
        if (!world.isRemote) {
            internalItemStackBuffer.add(new TubeStack(stack, getOutputDirection().getOpposite(), color));
            if (internalItemStackBuffer.size() == 1)
                ejectionScheduled = true;
            animationTicker = 0;
            sendUpdatePacket();
        }
    }

    public List<TubeStack> getBacklog() {
        return internalItemStackBuffer;
    }

    @SideOnly(Side.CLIENT)
    public void setBacklog(List<TubeStack> backlog) {
        internalItemStackBuffer.clear();
        internalItemStackBuffer.addAll(backlog);
    }

    protected void addItemToOutputBuffer(ItemStack stack) {

        addItemToOutputBuffer(stack, TubeColor.NONE);

    }

    protected void addItemsToOutputBuffer(Iterable<ItemStack> stacks) {

        addItemsToOutputBuffer(stacks, TubeColor.NONE);
    }

    protected void addItemsToOutputBuffer(Iterable<ItemStack> stacks, TubeColor color) {

        for (ItemStack stack : stacks) {
            addItemToOutputBuffer(stack, color);
        }
    }

    protected boolean isBufferEmpty() {

        return internalItemStackBuffer.isEmpty();// also say the buffer is empty when a immediate injection is scheduled.
    }

    public TileEntity getTileCache(EnumFacing d) {

        if (tileCache == null) {
            tileCache = new TileEntityCache(world, pos);
        }
        return tileCache.getValue(d);
    }

    public EnumFacing getOutputDirection() {

        return getFacingDirection().getOpposite();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("ItemBuffer", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);

            internalItemStackBuffer.add(TubeStack.loadFromNBT(nbttagcompound1));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        super.writeToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();

        for (TubeStack tubeStack : internalItemStackBuffer) {
            if (tubeStack != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                tubeStack.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        compound.setTag("ItemBuffer", nbttaglist);
        return compound;
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound compound) {

        super.writeToPacketNBT(compound);
        compound.setBoolean("animating", animationTicker >= 0);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound compound) {

        super.readFromPacketNBT(compound);
        boolean wasAnimating = isEjecting();
        isAnimating = compound.getBoolean("animating");
        if (isAnimating)
            animationTicker = 0;
        if (world != null && wasAnimating != isEjecting()) {
            markForRenderUpdate();
        }
    }

    public void ejectItemInWorld(ItemStack stack, EnumFacing oppDirection) {

        float spawnX = pos.getX() + 0.5F + oppDirection.getFrontOffsetX() * 0.8F;
        float spawnY = pos.getY() + 0.5F + oppDirection.getFrontOffsetY() * 0.8F;
        float spawnZ = pos.getZ() + 0.5F + oppDirection.getFrontOffsetZ() * 0.8F;

        EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);

        droppedItem.motionX = oppDirection.getFrontOffsetX() * 0.20F;
        droppedItem.motionY = oppDirection.getFrontOffsetY() * 0.20F;
        droppedItem.motionZ = oppDirection.getFrontOffsetZ() * 0.20F;

        world.spawnEntity(droppedItem);
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        for (TubeStack stack : internalItemStackBuffer)
            drops.add(stack.stack);
        return drops;
    }

    @Override
    public boolean isConnectedTo(EnumFacing from) {

        EnumFacing dir = getOutputDirection();
        return from == dir.getOpposite() || acceptsTubeItems && from == dir;
    }

    @Override
    public int getWeight(EnumFacing from) {

        return from == getOutputDirection().getOpposite() ? 1000000 : 0;// make the buffer side the last place to go
    }

    @Override
    public boolean isEjecting() {

        return isAnimating;
    }

    /**
     * Adds information to the waila tooltip
     *
     * @author Koen Beckers (K4Unl)
     *
     * @param info
     */
    @SideOnly(Side.CLIENT)
    public void addWailaInfo(List<String> info) {

        if (isEjecting()) {
            info.add(MinecraftColor.RED.getChatColor() + "[" + I18n.format("waila.machine.stuffed") + "]");
        }

    }
}
