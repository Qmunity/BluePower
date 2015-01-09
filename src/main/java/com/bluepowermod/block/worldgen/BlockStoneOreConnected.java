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

package com.bluepowermod.block.worldgen;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStoneOreConnected extends BlockStoneOre {

    @SideOnly(Side.CLIENT)
    public IIcon[] icons;
    public static int[] iconRefByID = { 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2,
            27, 14, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 4, 4, 5, 5,
            4, 4, 5, 5, 17, 17, 22, 26, 17, 17, 22, 26, 16, 16, 20, 20, 16, 16, 28, 28, 21, 21, 46, 42, 21, 21, 43, 38, 4, 4, 5, 5, 4, 4,
            5, 5, 9, 9, 30, 12, 9, 9, 30, 12, 16, 16, 20, 20, 16, 16, 28, 28, 25, 25, 45, 37, 25, 25, 40, 32, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3,
            19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19,
            15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 4, 4, 5, 5, 4, 4, 5, 5, 17, 17, 22, 26, 17, 17, 22, 26, 7, 7, 24,
            24, 7, 7, 10, 10, 29, 29, 44, 41, 29, 29, 39, 33, 4, 4, 5, 5, 4, 4, 5, 5, 9, 9, 30, 12, 9, 9, 30, 12, 7, 7, 24, 24, 7, 7, 10,
            10, 8, 8, 36, 35, 8, 8, 34, 11 };

    private String name;

    public BlockStoneOreConnected(String name) {

        super(name);
        this.name = name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        return icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        boolean[] bitMatrix = new boolean[8];

        if (side == 0 || side == 1) {
            bitMatrix[0] = world.getBlock(x - 1, y, z - 1) == this;
            bitMatrix[1] = world.getBlock(x, y, z - 1) == this;
            bitMatrix[2] = world.getBlock(x + 1, y, z - 1) == this;
            bitMatrix[3] = world.getBlock(x - 1, y, z) == this;
            bitMatrix[4] = world.getBlock(x + 1, y, z) == this;
            bitMatrix[5] = world.getBlock(x - 1, y, z + 1) == this;
            bitMatrix[6] = world.getBlock(x, y, z + 1) == this;
            bitMatrix[7] = world.getBlock(x + 1, y, z + 1) == this;
        }
        if (side == 2 || side == 3) {
            bitMatrix[0] = world.getBlock(x + (side == 2 ? 1 : -1), y + 1, z) == this;
            bitMatrix[1] = world.getBlock(x, y + 1, z) == this;
            bitMatrix[2] = world.getBlock(x + (side == 3 ? 1 : -1), y + 1, z) == this;
            bitMatrix[3] = world.getBlock(x + (side == 2 ? 1 : -1), y, z) == this;
            bitMatrix[4] = world.getBlock(x + (side == 3 ? 1 : -1), y, z) == this;
            bitMatrix[5] = world.getBlock(x + (side == 2 ? 1 : -1), y - 1, z) == this;
            bitMatrix[6] = world.getBlock(x, y - 1, z) == this;
            bitMatrix[7] = world.getBlock(x + (side == 3 ? 1 : -1), y - 1, z) == this;
        }
        if (side == 4 || side == 5) {
            bitMatrix[0] = world.getBlock(x, y + 1, z + (side == 5 ? 1 : -1)) == this;
            bitMatrix[1] = world.getBlock(x, y + 1, z) == this;
            bitMatrix[2] = world.getBlock(x, y + 1, z + (side == 4 ? 1 : -1)) == this;
            bitMatrix[3] = world.getBlock(x, y, z + (side == 5 ? 1 : -1)) == this;
            bitMatrix[4] = world.getBlock(x, y, z + (side == 4 ? 1 : -1)) == this;
            bitMatrix[5] = world.getBlock(x, y - 1, z + (side == 5 ? 1 : -1)) == this;
            bitMatrix[6] = world.getBlock(x, y - 1, z) == this;
            bitMatrix[7] = world.getBlock(x, y - 1, z + (side == 4 ? 1 : -1)) == this;
        }

        int idBuilder = 0;

        for (int i = 0; i <= 7; i++)
            idBuilder = idBuilder
                    + (bitMatrix[i] ? (i == 0 ? 1 : (i == 1 ? 2 : (i == 2 ? 4 : (i == 3 ? 8 : (i == 4 ? 16 : (i == 5 ? 32 : (i == 6 ? 64
                            : 128))))))) : 0);

        return idBuilder > 255 || idBuilder < 0 ? icons[0] : icons[iconRefByID[idBuilder]];

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        icons = new IIcon[iconRefByID.length];

        for (int i = 0; i < 47; i++)
            icons[i] = iconRegister.registerIcon(Refs.MODID + ":" + name + "/" + name + "_" + (i + 1));
    }

}
