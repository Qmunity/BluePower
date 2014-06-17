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

package net.quetzi.bluepower.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quetzi.bluepower.containers.ContainerAlloyFurnace;
import net.quetzi.bluepower.containers.ContainerBuffer;
import net.quetzi.bluepower.containers.ContainerSortingMachine;
import net.quetzi.bluepower.containers.ContainerSeedBag;
import net.quetzi.bluepower.items.ItemSeedBag;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.tileentities.tier1.TileAlloyFurnace;
import net.quetzi.bluepower.tileentities.tier1.TileBuffer;
import net.quetzi.bluepower.tileentities.tier2.TileSortingMachine;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    
        // This function creates a container
        TileEntity ent = world.getTileEntity(x, y, z);
        // ID is the GUI ID
        switch (GuiIDs.values()[ID]) {
            case ALLOY_FURNACE:
                return new ContainerAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
            case BUFFER:
                return new ContainerBuffer(player.inventory, (TileBuffer) ent);
            case SORTING_MACHINE:
                return new ContainerSortingMachine(player.inventory, (TileSortingMachine) ent);
            case SEEDBAG:
                if (player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemSeedBag)
                {
                    return new ContainerSeedBag(player.inventory,ItemSeedBag.getSeedBagInv(player));
                }
                break;
			default:
				break;
        }
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    
        TileEntity ent = world.getTileEntity(x, y, z);
        // ID is the GUI ID
        switch (GuiIDs.values()[ID]) {
            case ALLOY_FURNACE:
                return new GuiAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
            case BUFFER:
                return new GuiBuffer(player.inventory, (TileBuffer) ent);
            case SORTING_MACHINE:
                return new GuiSortingMachine(player.inventory, (TileSortingMachine) ent);
            case SEEDBAG:
                if (player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemSeedBag)
                {
                    return new GuiSeedBag(player.inventory,ItemSeedBag.getSeedBagInv(player));
                }
			default:
				break;
        }
        return null;
    }
}
