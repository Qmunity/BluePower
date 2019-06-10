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

import com.bluepowermod.BluePower;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.tile.IRotatable;
import com.bluepowermod.tile.TileBase;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author MineMaarten
 */

public class BlockContainerBase extends BlockBase implements IAdvancedSilkyRemovable {

    private GuiIDs guiId = GuiIDs.INVALID;
    private Class<? extends TileBase> tileEntityClass;
    private boolean isRedstoneEmitter;
    private boolean isSilkyRemoving;

    public BlockContainerBase(Material material, Class<? extends TileBase> tileEntityClass) {

        super(material);
        hasTileEntity = true;
        setTileEntityClass(tileEntityClass);
    }

    public BlockContainerBase setGuiId(GuiIDs guiId) {

        this.guiId = guiId;
        return this;
    }

    public BlockContainerBase setTileEntityClass(Class<? extends TileBase> tileEntityClass) {

        this.tileEntityClass = tileEntityClass;
        return this;
    }

    public BlockContainerBase emitsRedstone() {

        isRedstoneEmitter = true;
        return this;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        try {
            return getTileEntity().newInstance();
        } catch (Exception e) {
            return super.createTileEntity(world, state);
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

    protected TileBase get(IBlockAccess w, BlockPos pos) {

        TileEntity te = w.getTileEntity(pos);

        if (te == null || !(te instanceof TileBase))
            return null;

        return (TileBase) te;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        // Only do this on the server side.
        if (!((World)world).isRemote) {
            TileBase tileEntity = get(world, pos);
            if (tileEntity != null) {
                tileEntity.onBlockNeighbourChanged();
            }
        }
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return isRedstoneEmitter;
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockAccess par1IBlockAccess, BlockPos pos, Direction side) {
        TileEntity te = get(par1IBlockAccess, pos);
        if (te instanceof TileBase) {
            TileBase tileBase = (TileBase) te;
            return tileBase.getOutputtingRedstone();
        }
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!player.getHeldItem(hand).isEmpty()) {
                if (player.getHeldItem(hand).getItem() == BPItems.screwdriver) {
                    return false;
                }
            }
        }

        if (player.isSneaking()) {
            return false;
        }

        TileEntity entity = get(world, pos);
        if (entity == null || !(entity instanceof TileBase)) {
            return false;
        }

        if (getGuiID() != GuiIDs.INVALID) {
            if (!world.isRemote)
                player.openGui(BluePower.instance, getGuiID().ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, BlockState state) {
        if (!isSilkyRemoving) {
            TileBase tile = get(world, pos);
            if (tile != null) {
                for (ItemStack stack : tile.getDrops()) {
                    IOHelper.spawnItemInWorld(world, stack, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                }
            }
        }
        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }


    protected boolean canRotateVertical() {

        return true;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, Direction dir) {
        TileEntity te = get(world, pos);
        if (te instanceof IRotatable) {
            IRotatable rotatable = (IRotatable) te;
            if (dir != Direction.UP && dir != Direction.DOWN || canRotateVertical()) {
                rotatable.setFacingDirection(dir);
                return true;
            }
        }
        return false;
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public GuiIDs getGuiID() {

        return guiId;
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

        world.getTileEntity(pos).writeToNBT(tag);
        return false;
    }

    @Override
    public void readSilkyData(World world, BlockPos pos, CompoundNBT tag) {

        world.getTileEntity(pos).readFromNBT(tag);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockAccess world, BlockPos pos, @Nullable Direction side) {
        TileEntity te = world.getTileEntity(pos);
        return te != null && te instanceof TileBase && ((TileBase) te).canConnectRedstone();
    }
}
