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

package com.bluepowermod.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.tile.IRotatable;
import uk.co.qmunity.lib.tile.TileBase;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileProjectTable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockProjectTable extends BlockContainerBase {

    protected IIcon textureTop;
    protected IIcon textureBottom;
    protected IIcon textureSide;
    protected IIcon textureFront;

    public BlockProjectTable() {

        super(Material.wood, TileProjectTable.class);
        setBlockName(Refs.PROJECTTABLE_NAME);
    }

    public BlockProjectTable(Class<? extends TileBase> tileClass) {

        super(Material.wood, tileClass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        IRotatable rotatable = (IRotatable) world.getTileEntity(x, y, z);
        ForgeDirection s = ForgeDirection.getOrientation(side);
        // If is facing

        if (rotatable.getFacingDirection() == s) {
            return textureFront;
        }
        switch (s) {
        case UP:
            return textureTop;
        case DOWN:
            return textureBottom;
        case EAST:
        case NORTH:
        case SOUTH:
        case WEST:
        case UNKNOWN:
            return textureSide;
        default:
            break;

        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        ForgeDirection s = ForgeDirection.getOrientation(side);
        if (meta == side) {
            return textureFront;
        }
        switch (s) {
        case UP:
            return textureTop;
        case DOWN:
            return textureBottom;
        case EAST:
        case NORTH:
        case SOUTH:
        case WEST:
        case UNKNOWN:
            return textureSide;
        default:
            break;

        }
        return null;
    }

    @Override
    public boolean isOpaqueCube() {

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        textureTop = iconRegister.registerIcon(getTextureName() + "_top");
        textureBottom = iconRegister.registerIcon(getTextureName() + "_bottom");
        textureSide = iconRegister.registerIcon(getTextureName() + "_side");
        textureFront = iconRegister.registerIcon(getTextureName() + "_front");
    }

    @Override
    protected boolean canRotateVertical() {

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return 0;
    }
}
