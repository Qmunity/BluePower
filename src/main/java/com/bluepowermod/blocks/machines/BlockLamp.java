/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RenderLamp;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.tileentities.tier1.TileLamp;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl)
 * 
 */
public class BlockLamp extends BlockContainerBase {

    private boolean isInverted;
    private final String colorName;
    private int color;

    public BlockLamp(boolean _isInverted, String _colorName, int _color) {

        super(Material.iron, TileLamp.class);
        setInverted(_isInverted);
        colorName = _colorName;
        setColor(_color);
        setBlockName(Refs.LAMP_NAME + (isInverted ? "inverted" : "") + colorName);
        setCreativeTab(CustomTabs.tabBluePowerLighting);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        blockIcon = iconRegister.registerIcon(Refs.MODID + ":lamps/lamp_on");
        blockIcon = iconRegister.registerIcon(Refs.MODID + ":lamps/lamp_off");
    }

    @Override
    public int getLightValue(IBlockAccess w, int x, int y, int z) {

        TileLamp tileEntity = (TileLamp) w.getTileEntity(x, y, z);
        if (tileEntity != null) {
            int power = tileEntity.getPower();

            if (isInverted()) {
                power = 15 - power;
            }
            return power;
        } else {
            return 0;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderLamp.RENDER_ID;
    }

    public int getColor() {

        return color;
    }

    public void setColor(int color) {

        this.color = color;
    }

    public boolean isInverted() {

        return isInverted;
    }

    public void setInverted(boolean isInverted) {

        this.isInverted = isInverted;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        return true;
    }

    @Override
    public boolean canRenderInPass(int pass) {

        RenderLamp.pass = pass;

        return true;
    }

    @Override
    public int getRenderBlockPass() {

        return 1;
    }
}
