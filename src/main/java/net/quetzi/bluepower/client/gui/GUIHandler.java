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

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quetzi.bluepower.containers.ContainerAlloyFurnace;
import net.quetzi.bluepower.containers.ContainerBuffer;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.tileentities.tier1.TileAlloyFurnace;
import net.quetzi.bluepower.tileentities.tier1.TileBuffer;

public class GUIHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        // This function creates a container
        TileEntity ent = world.getTileEntity(x, y, z);
        if (ent != null) {
            // ID is the GUI ID
            if (ID == GuiIDs.ALLOY_FURNACE.ordinal()) {
                if (ent instanceof TileAlloyFurnace) { // Just a sanity check.
                    return new ContainerAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
                }
            }
            if (ID == GuiIDs.BUFFER.ordinal()) {
                if (ent instanceof TileBuffer) {
                    return new ContainerBuffer(player.inventory, (TileBuffer) ent);
                }
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        TileEntity ent = world.getTileEntity(x, y, z);
        if (ent != null) {
            // ID is the GUI ID
            if (ID == GuiIDs.ALLOY_FURNACE.ordinal()) {
                if (ent instanceof TileAlloyFurnace) { // Just a sanity check.
                    return new GuiAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
                }
            }
            if (ID == GuiIDs.BUFFER.ordinal()) {
                if (ent instanceof TileBuffer) {
                    return new GuiBuffer(player.inventory, (TileBuffer) ent);
                }
            }
        }
        return null;
    }

}
