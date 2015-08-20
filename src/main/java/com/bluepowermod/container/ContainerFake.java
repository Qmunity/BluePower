package com.bluepowermod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerFake extends Container {

    public ContainerFake(EntityPlayer player, int offX, int offY) {

        IInventory invPlayer = player.inventory;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18 + offX, 84 + i * 18 + offY));
            }
        }
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18 + offX, 142 + offY));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {

        return true;
    }

}
