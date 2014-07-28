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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import com.bluepowermod.client.renderers.RendererBlockBase;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.references.Refs;

public abstract class BlockBase extends Block {

    private String unlocalizedName;
    
    public BlockBase(Material material) {
    
        super(material);
        setStepSound(soundTypeStone);
        setCreativeTab(CustomTabs.tabBluePowerMachines);
        blockHardness = 3.0F;
    }

    @Override
    public String getUnlocalizedName() {

        return String.format("tile.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    /**
     * Method to detect how the block was placed, and what way it's facing.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack) {
    
        int sideToPlace;
        if (canRotateVertical() && player.rotationPitch > 45) {
            sideToPlace = 4;
        } else if (canRotateVertical() && player.rotationPitch < -45) {
            sideToPlace = 5;
        } else {
            sideToPlace = MathHelper.floor_double(player.rotationYaw / 90F + 0.5D) & 3;
        }
        
        int metaDataToSet = 0;
        switch (sideToPlace) {
            case 0:
                metaDataToSet = 2;
                break;
            case 1:
                metaDataToSet = 5;
                break;
            case 2:
                metaDataToSet = 3;
                break;
            case 3:
                metaDataToSet = 4;
                break;
            case 4:
                metaDataToSet = 1;
                break;
            case 5:
                metaDataToSet = 0;
                break;
        }
        
        world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
    }
    
    protected boolean canRotateVertical() {
    
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RendererBlockBase.RENDER_ID;
    }
}
