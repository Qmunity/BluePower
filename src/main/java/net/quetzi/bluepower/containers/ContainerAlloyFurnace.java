package net.quetzi.bluepower.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.slots.SlotMachineInput;
import net.quetzi.bluepower.slots.SlotMachineOutput;
import net.quetzi.bluepower.tileentities.tier1.TileAlloyFurnace;

public class ContainerAlloyFurnace extends Container
{

    private TileAlloyFurnace tileFurnace;

    public ContainerAlloyFurnace(InventoryPlayer invPlayer, TileAlloyFurnace furnace)
    {
        tileFurnace = furnace;


        addSlotToContainer(new SlotMachineInput(furnace, 0, 21, 35));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotMachineInput(furnace, (i * 3) + j + 2, 47 + (i * 18), 17 + (j * 18)));
            }
        }
        addSlotToContainer(new SlotMachineOutput(furnace, 1, 134, 35));

        bindPlayerInventory(invPlayer);

    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer)
    {
        //Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
            }
        }

        //Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + (j * 18), 142));
        }
    }

    // NOTE! This function does magic which i(K-4U) Do not completely understand.
    //Best ask MineMaarten to do this
    //TODO
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();


            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize) {
                return null;
            }

            slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return stack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return tileFurnace.isUseableByPlayer(entityplayer);
    }


}
