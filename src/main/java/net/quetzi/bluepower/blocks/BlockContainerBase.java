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
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.tileentities.TileBase;

public abstract class BlockContainerBase extends BlockBase implements ITileEntityProvider {

    public BlockContainerBase(Material material) {

        super(material);
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
     * Method to be overwritten to fetch the TileEntity Class that goes with the block
     *
     * @return a .class
     */
    protected abstract Class<? extends TileEntity> getTileEntity();

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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

        if (player.isSneaking()) {
            return false;
        }

        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity == null || !(entity instanceof TileBase)) {
            return false;
        }

        if (getGuiID() != GuiIDs.INVALID) {
            player.openGui(BluePower.instance, getGuiID().ordinal(), world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {

        TileBase tile = (TileBase) world.getTileEntity(x, y, z);
        for (ItemStack stack : tile.getDrops()) {
            IOHelper.spawnItemInWorld(world, stack, x + 0.5F, y + 0.5F, z + 0.5F);
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    /**
     * Method to be overwritten that returns a GUI ID
     *
     * @return
     */
    public abstract GuiIDs getGuiID();
}
