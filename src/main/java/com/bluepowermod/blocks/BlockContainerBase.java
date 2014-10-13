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

import net.minecraft.block.Block;
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
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.IBluePowered;
import com.bluepowermod.tileentities.IEjectAnimator;
import com.bluepowermod.util.Refs;
import com.qmunity.lib.block.BlockTileBase;
import com.qmunity.lib.tileentity.IRotatable;
import com.qmunity.lib.tileentity.TileBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */

public class BlockContainerBase extends BlockTileBase implements IAdvancedSilkyRemovable {

    @SideOnly(Side.CLIENT)
    protected Map<String, IIcon> textures;

    private boolean isSilkyRemoving;

    public BlockContainerBase(Material material, Class<? extends TileBase> tileEntityClass) {
        super(material, tileEntityClass);
        setCreativeTab(CustomTabs.tabBluePowerMachines);
    }

    public BlockContainerBase setGuiId(GuiIDs guiId) {

        this.setGuiId(guiId.ordinal());
        return this;
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

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    @Override
    protected boolean shouldDropItems() {
        return !isSilkyRemoving;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack) {

        BPApi.getInstance().loadSilkySettings(world, x, y, z, iStack);
        super.onBlockPlacedBy(world, x, y, z, player, iStack);
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

    @Override
    protected Object getModInstance() {
        return BluePower.instance;
    }

    @Override
    protected String getModId() {
        return Refs.MODID;
    }
}
