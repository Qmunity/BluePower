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
import com.bluepowermod.helper.BlockEntityCache;
import com.bluepowermod.container.stack.TubeStack;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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
    private BlockEntityCache tileCache;
    public static final int BUFFER_EMPTY_INTERVAL = 10;
    protected byte animationTicker = -1;
    protected static final int ANIMATION_TIME = 7;
    private boolean isAnimating;
    protected boolean ejectionScheduled;
    private static final int WARNING_INTERVAL = 600; // Every 30s

    public TileMachineBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tickMachineBase(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        tickTileBase(level, pos, state, blockEntity);
        if (!level.isClientSide && blockEntity instanceof TileMachineBase) {
            TileMachineBase machineBase = (TileMachineBase) blockEntity;
            if (machineBase.ejectionScheduled || machineBase.getTicker() % BUFFER_EMPTY_INTERVAL == 0) {
                machineBase.ejectItems();
                machineBase.ejectionScheduled = false;
            }
        }
    }

    private void ejectItems() {

        for (Iterator<TubeStack> iterator = internalItemStackBuffer.iterator(); iterator.hasNext();) {
            TubeStack tubeStack = iterator.next();
            if (IOHelper.canInterfaceWith(getTileCache(getOutputDirection()), getFacingDirection())) {
                ItemStack returnedStack = IOHelper.insert(getTileCache(getOutputDirection()), tubeStack.stack, getFacingDirection(), tubeStack.color, false);
                if (returnedStack.isEmpty()) {
                    iterator.remove();
                    setChanged();
                    if (!ejectionScheduled)
                        break;
                } else if (returnedStack.getCount() != tubeStack.stack.getCount()) {
                    setChanged();
                    if (!ejectionScheduled)
                        break;
                } else {
                    break;
                }
            } else if (spawnItemsInWorld) {
                Direction direction = getFacingDirection().getOpposite();
                Block block = level.getBlockState(worldPosition.relative(direction)).getBlock();
                if (!level.getBlockState( worldPosition.relative(direction)).canOcclude() || block instanceof LiquidBlock) {
                    ejectItemInWorld(tubeStack.stack, direction);
                    iterator.remove();
                    setChanged();
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public void clearRemoved() {

        super.clearRemoved();
        tileCache = null;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        tileCache = null;
    }

    protected void addItemToOutputBuffer(ItemStack stack, TubeColor color) {
        if (!level.isClientSide) {
            internalItemStackBuffer.add(new TubeStack(stack, getOutputDirection().getOpposite(), color));
            if (internalItemStackBuffer.size() == 1)
                ejectionScheduled = true;
            animationTicker = 0;
            sendUpdatePacket();
            setChanged();
        }
    }

    public List<TubeStack> getBacklog() {
        return internalItemStackBuffer;
    }

    @OnlyIn(Dist.CLIENT)
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

    public BlockEntity getTileCache(Direction d) {

        if (tileCache == null) {
            tileCache = new BlockEntityCache(level, worldPosition);
        }
        return tileCache.getValue(d);
    }

    public Direction getOutputDirection() {

        return getFacingDirection().getOpposite();
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {

        super.loadAdditional(compound, provider);
        ListTag nbttaglist = compound.getList("ItemBuffer", 10);

        for (int i = 0; i < nbttaglist.size(); ++i) {
            CompoundTag nbttagcompound1 = nbttaglist.getCompound(i);

            internalItemStackBuffer.add(TubeStack.loadFromNBT(provider, nbttagcompound1));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {

        super.saveAdditional(compound, provider);
        ListTag nbttaglist = new ListTag();

        for (TubeStack tubeStack : internalItemStackBuffer) {
            if (tubeStack != null) {
                CompoundTag nbttagcompound1 = new CompoundTag();
                tubeStack.writeToNBT(provider, nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }
        compound.put("ItemBuffer", nbttaglist);
    }

    public void ejectItemInWorld(ItemStack stack, Direction oppDirection) {

        float spawnX = worldPosition.getX() + 0.5F + oppDirection.getStepX() * 0.8F;
        float spawnY = worldPosition.getY() + 0.5F + oppDirection.getStepY() * 0.8F;
        float spawnZ = worldPosition.getZ() + 0.5F + oppDirection.getStepZ() * 0.8F;

        ItemEntity droppedItem = new ItemEntity(level, spawnX, spawnY, spawnZ, stack);
        droppedItem.setDeltaMovement(oppDirection.getStepX() * 0.20F, oppDirection.getStepY() * 0.20F, oppDirection.getStepZ() * 0.20F);
        level.addFreshEntity(droppedItem);
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        for (TubeStack stack : internalItemStackBuffer)
            drops.add(stack.stack);
        return drops;
    }

    @Override
    public boolean isConnectedTo(Direction from) {

        Direction dir = getOutputDirection();
        return from == dir.getOpposite() || acceptsTubeItems && from == dir;
    }

    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, Direction from, boolean simulate) {
        if(!simulate) {
            internalItemStackBuffer.add(stack);
        }
        return stack;
    }

    @Override
    public int getWeight(Direction from) {

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
    @OnlyIn(Dist.CLIENT)
    public void addWailaInfo(List<String> info) {

        if (isEjecting()) {
            info.add(MinecraftColor.RED.getChatColor() + "[" + I18n.get("waila.machine.stuffed") + "]");
        }

    }
}
