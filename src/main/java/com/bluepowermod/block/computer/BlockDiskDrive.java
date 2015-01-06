/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.computer;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.tile.tier3.TileDiskDrive;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDiskDrive extends BlockContainerBase {

    @SideOnly(Side.CLIENT)
    protected IIcon topTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon frontTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon sideTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon backTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon bottomTexture;

    public BlockDiskDrive() {

        super(Material.iron, TileDiskDrive.class);
        setBlockName(Refs.BLOCKDISKDRIVE_NAME);
    }

    @Override
    public GuiIDs getGuiID() {

        return GuiIDs.DISK_DRIVE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        ForgeDirection dir = ForgeDirection.getOrientation(meta);
        if (side == dir.ordinal()) {
            return topTexture;
        } else if (side == dir.getOpposite().ordinal()) {
            return bottomTexture;
        } else if (side == ForgeDirection.WEST.ordinal()) {
            return frontTexture;
        } else if (side == ForgeDirection.EAST.ordinal()) {
            return backTexture;
        }
        return sideTexture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        TileDiskDrive tile = (TileDiskDrive) world.getTileEntity(x, y, z);
        ForgeDirection dir = tile.getFacingDirection();

        if (dir.ordinal() == side) {
            return frontTexture;
        } else if (dir.getOpposite().ordinal() == side) {
            return backTexture;
        } else if (ForgeDirection.UP.ordinal() == side) {
            return topTexture;
        } else if (ForgeDirection.DOWN.ordinal() == side) {
            return bottomTexture;
        } else {
            return sideTexture;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        frontTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "disk_drive_front");
        sideTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_side");
        topTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_top");
        backTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_back");
        bottomTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_bottom");
    }

}
