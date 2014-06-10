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

package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.tileentities.TileBase;

public abstract class BlockBase extends Block {

    public BlockBase(Material material) {

        super(material);
        this.setStepSound(soundTypeStone);
        this.setCreativeTab(CustomTabs.tabBluePowerMachines);
        this.blockHardness = 3.0F;
    }

    protected abstract Class<? extends TileEntity> getTileEntity();

    /**
     * Method to be overwritten that returns a GUI ID
     * 
     * @return
     */
    public abstract GuiIDs getGuiID();

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

    /**
     * Method to detect how the block was placed, and what way it's facing.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack) {

        int sideToPlace = MathHelper.floor_double(player.rotationYaw / 90F + 0.5D) & 3;

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
        }

        world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
    }
}
