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
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author MineMaarten
 */

public class BlockContainerBase extends BlockBase implements IAdvancedSilkyRemovable {

    private Class<? extends TileBase> tileEntityClass;
    private boolean isRedstoneEmitter;
    private boolean isSilkyRemoving;

    public BlockContainerBase(Material material, Class<? extends TileBase> tileEntityClass) {
        super(material);
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
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops =  super.getDrops(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof TileBase) {
                InventoryHelper.dropContents(worldIn, pos, ((TileBase)tileentity).getDrops());
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        try {
            return getTileEntity().newInstance();
        } catch (Exception e) {
            return super.createTileEntity(state, world);
        }
    }

    /**
     * Fetches the TileEntity Class that goes with the block
     *
     * @return a .class
     */
    protected Class<? extends TileEntity> getTileEntity() {

        return tileEntityClass;
    }

    protected TileBase get(IBlockReader w, BlockPos pos) {

        TileEntity te = w.getBlockEntity(pos);

        if (te == null || !(te instanceof TileBase))
            return null;

        return (TileBase) te;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        // Only do this on the server side.
        if (!world.isClientSide) {
            TileBase tileEntity = get(world, pos);
            if (tileEntity != null) {
                tileEntity.onBlockNeighbourChanged();
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return isRedstoneEmitter;
    }

    @Override
    public int getSignal(BlockState blockState, IBlockReader par1IBlockReader, BlockPos pos, Direction side) {
        TileEntity te = get(par1IBlockReader, pos);
        if (te instanceof TileBase) {
            TileBase tileBase = (TileBase) te;
            return tileBase.getOutputtingRedstone();
        }
        return 0;
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        if (player.isCrouching()) {
            if (!player.getItemInHand(hand).isEmpty()) {
                if (player.getItemInHand(hand).getItem() == BPItems.screwdriver) {
                    return ActionResultType.FAIL;
                }
            }
        }
        if (player.isCrouching()) {
            return ActionResultType.FAIL;
        }
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider)world.getBlockEntity(pos));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    protected boolean canRotateVertical() {

        return true;
    }


    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean preSilkyRemoval(World world, BlockPos pos) {

        isSilkyRemoving = true;
        return true;
    }

    @Override
    public void postSilkyRemoval(World world, BlockPos pos) {

        isSilkyRemoving = false;
    }

    @Override
    public boolean writeSilkyData(World world, BlockPos pos, CompoundNBT tag) {

        world.getBlockEntity(pos).save(tag);
        return false;
    }

    @Override
    public void readSilkyData(World world, BlockPos pos, CompoundNBT tag) {

        world.getBlockEntity(pos).load(world.getBlockState(pos), tag);
    }

}
