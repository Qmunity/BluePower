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

package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPMenuType;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import com.bluepowermod.tile.tier3.TileKineticGenerator;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerKinect extends AbstractContainerMenu {

    private final Container kinect;

    public ContainerKinect(int windowId, Inventory invPlayer, Container inventory) {
        super(BPMenuType.KINETIC_GENERATOR, windowId);
        kinect = inventory;
        
        //Inventory for Turbines
        addSlot(new Slot(kinect, 0, 80, 35));
        
        bindPlayerInventory(invPlayer);
    }

    public ContainerKinect( int id, Inventory player )    {
        this( id, player, new SimpleContainer( TileKineticGenerator.SLOTS ));
    }


    protected void bindPlayerInventory(Inventory invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 8 + j * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {

        return kinect.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int par2) {
    	
    	return ItemStack.EMPTY;
     }
    
    
}
