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

package net.quetzi.bluepower.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.quetzi.bluepower.blocks.BlockContainerBase;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileRelay;

public class BlockRelay extends BlockContainerBase {

    public BlockRelay() {

        super(Material.rock);
        this.setBlockName(Refs.RELAY_NAME);
        this.setCreativeTab(CustomTabs.tabBluePowerMachines);
    }

    /**
     * Method to be overwritten to fetch the TileEntity Class that goes with the block
     *
     * @return a .class
     */
    @Override protected Class<? extends TileEntity> getTileEntity() {

        return TileRelay.class;
    }

    /**
     * Method to be overwritten that returns a GUI ID
     *
     * @return
     */
    @Override public GuiIDs getGuiID() {

        return GuiIDs.RELAY_ID;
    }
}
