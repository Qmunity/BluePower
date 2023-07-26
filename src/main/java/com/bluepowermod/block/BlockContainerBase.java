/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block;

import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

import javax.annotation.Nullable;


/**
 * @author MineMaarten
 */

public class BlockContainerBase extends BlockBase implements IAdvancedSilkyRemovable, EntityBlock {

    private Class<? extends TileBase> tileEntityClass;
    private boolean isRedstoneEmitter;
    private boolean isSilkyRemoving;

    public BlockContainerBase(Class<? extends TileBase> tileEntityClass) {
        setBlockEntityClass(tileEntityClass);
    }

    public BlockContainerBase(Properties properties, Class<? extends TileBase> tileEntityClass) {
        super(properties);
        setBlockEntityClass(tileEntityClass);
    }

    public BlockContainerBase setBlockEntityClass(Class<? extends TileBase> tileEntityClass) {

        this.tileEntityClass = tileEntityClass;
        return this;
    }

    public BlockContainerBase emitsRedstone() {

        isRedstoneEmitter = true;
        return this;
    }


    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops =  super.getDrops(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof TileBase) {
                Containers.dropContents(worldIn, pos, ((TileBase)tileentity).getDrops());
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    /**
     * Fetches the BlockEntity Class that goes with the block
     *
     * @return a .class
     */
    protected Class<? extends BlockEntity> getBlockEntity() {

        return tileEntityClass;
    }

    protected TileBase get(BlockGetter w, BlockPos pos) {

        BlockEntity te = w.getBlockEntity(pos);

        if (!(te instanceof TileBase))
            return null;

        return (TileBase) te;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        try {
            return getBlockEntity().getDeclaredConstructor(BlockPos.class, BlockState.class).newInstance(pos, state);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : TileMachineBase::tickMachineBase;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        // Only do this on the server side.
        if (!world.isClientSide) {
            TileBase tileEntity = get(world, pos);
            if (tileEntity != null) {
                TileBase.setChanged(world, pos, state, tileEntity);
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return isRedstoneEmitter;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter par1BlockGetter, BlockPos pos, Direction side) {
        TileBase te = get(par1BlockGetter, pos);
        if (te != null) {
            return te.getOutputtingRedstone();
        }
        return 0;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (player.isCrouching()) {
            if (!player.getItemInHand(hand).isEmpty()) {
                if (player.getItemInHand(hand).getItem() == BPItems.screwdriver.get()) {
                    return InteractionResult.FAIL;
                }
            }
        }
        if (player.isCrouching()) {
            return InteractionResult.FAIL;
        }
        if (world.getBlockEntity(pos) instanceof MenuProvider) {
            NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider)world.getBlockEntity(pos));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    protected boolean canRotateVertical() {

        return true;
    }


    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean preSilkyRemoval(Level world, BlockPos pos) {

        isSilkyRemoving = true;
        return true;
    }

    @Override
    public void postSilkyRemoval(Level world, BlockPos pos) {

        isSilkyRemoving = false;
    }

    @Override
    public boolean writeSilkyData(Level world, BlockPos pos, CompoundTag tag) {

        world.getBlockEntity(pos).saveWithFullMetadata();
        return false;
    }

    @Override
    public void readSilkyData(Level world, BlockPos pos, CompoundTag tag) {
        world.getBlockEntity(pos).load(tag);
    }

}
