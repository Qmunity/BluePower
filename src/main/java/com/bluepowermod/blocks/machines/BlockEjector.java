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
 *
 *     @author Quetzi
 */

package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.IRotatable;
import com.bluepowermod.tileentities.tier1.TileEjector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEjector extends BlockContainerBase {
    
    public BlockEjector() {
    
        super(Material.rock);
        setBlockName(Refs.EJECTOR_NAME);
        setCreativeTab(CustomTabs.tabBluePowerMachines);
    }
    
    /**
     * Method to be overwritten to fetch the TileEntity Class that goes with the block
     *
     * @return a .class
     */
    @Override
    protected Class<? extends TileEntity> getTileEntity() {
    
        return TileEjector.class;
    }
    
    /**
     * Method to be overwritten that returns a GUI ID
     *
     * @return
     */
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.EJECTOR_ID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        super.registerBlockIcons(iconRegister);
        blockIcon = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.EJECTOR_NAME + "_side_0");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
    
        return side == 2 || side == 3 ? blockIcon : super.getIcon(side, meta);
    }
    
    @Override
    protected IIcon getIcon(EnumFaceType faceType, boolean ejecting, boolean powered, int side, TileEntity te) {
    
        if (faceType == EnumFaceType.SIDE) {
            ForgeDirection orientation = ((IRotatable) te).getFacingDirection();
            if (orientation.ordinal() < 2) {
                if (side == ForgeDirection.WEST.ordinal() || side == ForgeDirection.EAST.ordinal()) return blockIcon;
            } else {
                if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal()) return blockIcon;
            }
        }
        return super.getIcon(faceType, ejecting, powered, side, te);
    }
    
}
