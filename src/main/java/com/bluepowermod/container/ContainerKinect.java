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

import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.tile.tier3.TileKinectGenerator;

public class ContainerKinect extends Container {

    private final IInventory kinect;

    public ContainerKinect(int windowId, PlayerInventory invPlayer, IInventory inventory) {
        super(BPContainerType.KINETIC_GENERATOR, windowId);
        kinect = inventory;
        
        //Inventory for Turbines
        addSlot(new Slot(kinect, 0, 80, 35));
        
        bindPlayerInventory(invPlayer);
    }

    public ContainerKinect( int id, PlayerInventory player )    {
        this( id, player, new Inventory( TileKinectGenerator.SLOTS ));
    }


    protected void bindPlayerInventory(PlayerInventory invPlayer) {

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
    public boolean canInteractWith(PlayerEntity player) {

        return kinect.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int par2) {
    	
    	return ItemStack.EMPTY;
     }
    
    
}
