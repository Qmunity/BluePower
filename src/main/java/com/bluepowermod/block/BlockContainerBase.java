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
import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.tile.IRotatable;
import com.bluepowermod.tile.TileBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author MineMaarten
 */

public class BlockContainerBase extends BlockBase implements ITileEntityProvider, IAdvancedSilkyRemovable {

    private GuiIDs guiId = GuiIDs.INVALID;
    private Class<? extends TileBase> tileEntityClass;
    private boolean isRedstoneEmitter;
    private boolean isSilkyRemoving;

    public BlockContainerBase(Material material, Class<? extends TileBase> tileEntityClass) {

        super(material);
        isBlockContainer = true;
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
    public TileEntity createNewTileEntity(World world, int metadata) {

        try {
            return getTileEntity().newInstance();
        } catch (Exception e) {
            return null;
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
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        // Only do this on the server side.
        if (!((World)world).isRemote) {
            TileBase tileEntity = get(world, pos);
            if (tileEntity != null) {
                tileEntity.onBlockNeighbourChanged();
            }
        }
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return isRedstoneEmitter;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess par1IBlockAccess, BlockPos pos, EnumFacing side) {
        TileEntity te = get(par1IBlockAccess, pos);
        if (te instanceof TileBase) {
            TileBase tileBase = (TileBase) te;
            return tileBase.getOutputtingRedstone();
        }
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
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
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
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

    /**
     * Method to detect how the block was placed, and what way it's facing.
     */
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack iStack) {
        BPApi.getInstance().loadSilkySettings(world, pos, iStack);
        TileEntity te = get(world, pos);
        if (te instanceof IRotatable) {
            ((IRotatable) te).setFacingDirection(placer.getHorizontalFacing().getOpposite());
        }
    }

    protected boolean canRotateVertical() {

        return true;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing dir) {
        TileEntity te = get(world, pos);
        if (te instanceof IRotatable) {
            IRotatable rotatable = (IRotatable) te;
            if (dir != EnumFacing.UP && dir != EnumFacing.DOWN || canRotateVertical()) {
                rotatable.setFacingDirection(dir);
                return true;
            }
        }
        return false;
    }
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
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
    public boolean writeSilkyData(World world, BlockPos pos, NBTTagCompound tag) {

        world.getTileEntity(pos).writeToNBT(tag);
        return false;
    }

    @Override
    public void readSilkyData(World world, BlockPos pos, NBTTagCompound tag) {

        world.getTileEntity(pos).readFromNBT(tag);
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        return te != null && te instanceof TileBase && ((TileBase) te).canConnectRedstone();
    }
}
