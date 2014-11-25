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

package com.bluepowermod.blocks;

import java.util.HashMap;
import java.util.Map;

import uk.co.qmunity.lib.misc.ForgeDirectionUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.client.renderers.RendererBlockBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.IBluePowered;
import com.bluepowermod.tileentities.IEjectAnimator;
import com.bluepowermod.tileentities.IRotatable;
import com.bluepowermod.tileentities.TileBase;
import com.bluepowermod.util.Refs;

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

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        super.onNeighborBlockChange(world, x, y, z, block);
        // Only do this on the server side.
        if (!world.isRemote) {
            TileBase tileEntity = (TileBase) world.getTileEntity(x, y, z);
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
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {

        TileEntity te = par1IBlockAccess.getTileEntity(par2, par3, par4);
        if (te instanceof TileBase) {
            TileBase tileBase = (TileBase) te;
            return tileBase.getOutputtingRedstone();
        }
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

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

        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity == null || !(entity instanceof TileBase)) {
            return false;
        }

        if (getGuiID() != GuiIDs.INVALID) {
            if (!world.isRemote)
                player.openGui(BluePower.instance, getGuiID().ordinal(), world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {

        if (!isSilkyRemoving) {
            TileBase tile = (TileBase) world.getTileEntity(x, y, z);
            if (tile != null) {
                for (ItemStack stack : tile.getDrops()) {
                    IOHelper.spawnItemInWorld(world, stack, x + 0.5, y + 0.5, z + 0.5);
                }
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
        world.removeTileEntity(x, y, z);
    }

    /**
     * Method to detect how the block was placed, and what way it's facing.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack) {

        BPApi.getInstance().loadSilkySettings(world, x, y, z, iStack);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof IRotatable) {
            ((IRotatable) te).setFacingDirection(ForgeDirectionUtils.getDirectionFacing(player, canRotateVertical()).getOpposite());
        }
    }

    protected boolean canRotateVertical() {

        return true;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {

        TileEntity te = worldObj.getTileEntity(x, y, z);
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
                } while (powered == true && IBluePowered.class.isAssignableFrom(getTileEntity()));
                ejecting = !ejecting;
            } while (ejecting == true && IEjectAnimator.class.isAssignableFrom(getTileEntity()));
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
        TileEntity te = world.getTileEntity(x, y, z);
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
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        return ((TileBase) world.getTileEntity(x, y, z)).canConnectRedstone();
    }
}
