/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileSortron;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

;

/**
 * @author Dynious
 */
public class BlockSortron extends BlockContainerBase {
    
    private final TextureAtlasSprite[] icons = new TextureAtlasSprite[8];
    
    public BlockSortron() {
    
        super(Material.ROCK, TileSortron.class);
        setBlockName(Refs.BLOCKSORTRON_NAME);
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.INVALID;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(int side, int meta) {
    
        EnumFacing direction = EnumFacing.getFront(meta);
        if (side == direction.ordinal()) {
            return icons[0];
        } else if (side == direction.getOpposite().ordinal()) { return icons[1]; }
        return icons[2];
    }
    
    /* @Override
     @SideOnly(Side.CLIENT)
     public TextureAtlasSprite getIcon(IBlockAccess world, int x, int y, int z, int side) {
     
         
                 TileSortron tile = (TileSortron) world.getTileEntity(x, y, z);
                 EnumFacing dir = tile.getFacingDirection();
                 if (dir.ordinal() == side) {
                     return icons[0];
                 } else if (dir.getOpposite().ordinal() == side) {
                     return icons[1];
                 } else if ((side == 1 && dir.ordinal() != 1 && dir.getOpposite().ordinal() != 1) || (side == 3 && (dir.ordinal() == 1 || dir.getOpposite().ordinal() == 1))) { // && isPowered
                     return icons[6];
                 } else if (tile.showOutPutAnimation()) { // && isPowered
                     return icons[4];
                 } else { // && isPowered
                     return icons[3];
                 }
         //TODO: different icons when powered
     }*/
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureMap iconRegister) {
    
        icons[0] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKSORTRON_NAME + "_front");
        icons[1] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKSORTRON_NAME + "_back");
        icons[2] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKSORTRON_NAME + "_side_off");
        icons[3] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKSORTRON_NAME + "_side_on");
        icons[4] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKSORTRON_NAME + "_side_on_1");
        icons[5] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKSORTRON_NAME + "_side");
        icons[6] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.BLOCKSORTRON_NAME + "_side_active");
    }
}
