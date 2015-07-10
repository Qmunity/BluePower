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

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.helper.BlockPos;
import uk.co.qmunity.lib.misc.ForgeDirectionUtils;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.client.render.RendererBlockBase;
import com.bluepowermod.client.render.RendererBlockBase.EnumFaceType;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.IBluePowered;
import com.bluepowermod.tile.IEjectAnimator;
import com.bluepowermod.tile.IRotatable;
import com.bluepowermod.tile.TileBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */

public class BlockContainerBase extends BlockBase implements ITileEntityProvider, IAdvancedSilkyRemovable {

    @SideOnly(Side.CLIENT)
    protected Map<String, IIcon> textures;
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

    protected TileBase get(IBlockAccess w, int x, int y, int z) {

        return get(w, new BlockPos(x, y, z));
    }

    protected TileBase get(IBlockAccess world, BlockPos pos) {

        TileEntity te = world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());

        if (te == null || !(te instanceof TileBase))
            return null;
        return (TileBase)te;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        onNeighborBlockChange(world, new BlockPos(x, y, z), block);
    }

    public void onNeighborBlockChange(World world, BlockPos pos, Block block) {

        super.onNeighborBlockChange(world, pos.getX(), pos.getY(), pos.getZ(), block);
        //Only do this on the server side.
        if (!world.isRemote) {
            TileBase tileEntity = get(world, pos);
            if (tileEntity != null) {
                tileEntity.onBlockNeighbourChanged();
            }
        }
    }

    @Override
    public boolean canProvidePower() {

        return isRedstoneEmitter;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int par5) {

        return isProvidingWeakPower(world, new BlockPos(x, y, z), par5);
    }

    public int isProvidingWeakPower(IBlockAccess world, BlockPos pos, int par3) {

        TileEntity te = get(world, pos.getX(), pos.getY(), pos.getZ());
        if (te instanceof TileBase) {
            TileBase tileBase = (TileBase) te;
            return tileBase.getOutputtingRedstone();
        }
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float hitX, float hitY, float hitZ) {

        return onBlockActivated(world, new BlockPos(x, y, z), player, par6, hitX, hitY, hitZ);
    }

    public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int par6, float hitX, float hitY, float hitZ) {

        if (player.isSneaking()) {
            if (player.getHeldItem() != null) {
                if (player.getHeldItem().getItem() == BPItems.screwdriver) {
                    return false;
                }
            }
        }

        if (player.isSneaking()) {
            return false;
        }

        TileEntity entity = get(world, pos.getX(), pos.getY(), pos.getZ());
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {

        breakBlock(world, new BlockPos(x, y, z), block, meta);
    }

    public void breakBlock(World world, BlockPos pos, Block block, int meta) {

        if (!isSilkyRemoving) {
            TileBase tile = get(world, pos.getX(), pos.getY(), pos.getZ());
            if (tile != null) {
                for (ItemStack stack : tile.getDrops()) {
                    IOHelper.spawnItemInWorld(world, stack, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                }
            }
        }
        super.breakBlock(world, pos.getX(), pos.getY(), pos.getZ(), block, meta);
        world.removeTileEntity(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Method to detect how the block was placed, and what way it's facing.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {

        onBlockPlacedBy(world, new BlockPos(x, y, z), player, stack);
    }

    public void onBlockPlacedBy(World world, BlockPos pos, EntityLivingBase player, ItemStack stack) {

        BPApi.getInstance().loadSilkySettings(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        TileEntity te = get(world, pos.getX(), pos.getY(), pos.getZ());
        if (te instanceof IRotatable) {
            ((IRotatable) te).setFacingDirection(ForgeDirectionUtils.getDirectionFacing(player, canRotateVertical()).getOpposite());
        }
    }

    protected boolean canRotateVertical() {

        return true;
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {

        return rotateBlock(world, new BlockPos(x, y, z), axis);
    }

    public boolean rotateBlock(World world, BlockPos pos, ForgeDirection axis) {

        TileEntity te = get(world, pos.getX(), pos.getY(), pos.getZ());
        if (te instanceof IRotatable) {
            IRotatable rotatable = (IRotatable) te;
            ForgeDirection dir = rotatable.getFacingDirection();
            dir = dir.getRotation(axis);
            if (dir != ForgeDirection.UP && dir != ForgeDirection.DOWN || canRotateVertical()) {
                rotatable.setFacingDirection(dir);
                return true;
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        textures = new HashMap<String, IIcon>();
        for (EnumFaceType faceType : EnumFaceType.values()) {
            boolean ejecting = false;
            boolean powered = false;

            do {
                do {
                    String iconName = getIconName(faceType, ejecting, powered);
                    if (!textures.containsKey(iconName)) {
                        textures.put(iconName, iconRegister.registerIcon(iconName));
                    }

                    powered = !powered;
                } while (powered && IBluePowered.class.isAssignableFrom(getTileEntity()));
                ejecting = !ejecting;
            } while (ejecting && IEjectAnimator.class.isAssignableFrom(getTileEntity()));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(EnumFaceType faceType, boolean ejecting, boolean powered) {

        return textures.get(getIconName(faceType, ejecting, powered));
    }

    @SideOnly(Side.CLIENT)
    protected IIcon getIcon(EnumFaceType faceType, boolean ejecting, boolean powered, int side, TileEntity te) {

        return getIcon(faceType, ejecting, powered);
    }

    @Override
    public Block setBlockName(String name) {

        super.setBlockName(name);
        textureName = Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + name;
        return this;
    }

    protected String getIconName(EnumFaceType faceType, boolean ejecting, boolean powered) {

        String iconName = textureName + "_" + faceType.toString().toLowerCase();
        if (faceType == EnumFaceType.SIDE) {
            if (ejecting)
                iconName += "_active";

            // TODO: When powersystem is implemented, uncomment this!
            // if (powered) iconName += "_powered";
        }
        return iconName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        if (textures == null)
            return super.getIcon(world, x, y, z, side);
        TileEntity te = get(world, x, y, z);
        RendererBlockBase.EnumFaceType faceType = EnumFaceType.SIDE;
        boolean powered = false;
        boolean ejecting = false;
        if (te instanceof IRotatable) {
            ForgeDirection rotation = ((IRotatable) te).getFacingDirection();
            if (rotation.ordinal() == side)
                faceType = EnumFaceType.FRONT;
            if (rotation.getOpposite().ordinal() == side)
                faceType = EnumFaceType.BACK;
        }
        if (te instanceof IBluePowered) {
            powered = ((IBluePowered) te).isPowered();
        }
        if (te instanceof IEjectAnimator) {
            ejecting = ((IEjectAnimator) te).isEjecting();
        }
        return getIcon(faceType, ejecting, powered, side, te);
    }

    /**
     * This method will only be called from the item render method. the meta variable doesn't have any meaning.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        if (textures == null)
            return super.getIcon(side, meta);
        return getIcon(EnumFaceType.values()[side == 0 ? 2 : side == 1 ? 1 : 0], false, false);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RendererBlockBase.RENDER_ID;
    }

    public GuiIDs getGuiID() {

        return guiId;
    }

    @Override
    public boolean preSilkyRemoval(World world, int x, int y, int z) {

        isSilkyRemoving = true;
        return true;
    }

    @Override
    public void postSilkyRemoval(World world, int x, int y, int z) {

        isSilkyRemoving = false;
    }

    @Override
    public boolean writeSilkyData(World world, int x, int y, int z, NBTTagCompound tag) {

        world.getTileEntity(x, y, z).writeToNBT(tag);
        return false;
    }

    @Override
    public void readSilkyData(World world, int x, int y, int z, NBTTagCompound tag) {

        world.getTileEntity(x, y, z).readFromNBT(tag);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        return canConnectRedstone(world, new BlockPos(x, y, z), side);
    }

    public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, int side) {

        return ((TileBase)world.getTileEntity(pos.getX(), pos.getY(), pos.getZ())).canConnectRedstone();
    }
}
