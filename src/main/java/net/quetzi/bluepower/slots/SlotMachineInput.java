package net.quetzi.bluepower.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMachineInput extends Slot
{

    IInventory inv;

    public SlotMachineInput(IInventory _inv, int slotNum, int x, int y)
    {
        super(_inv, slotNum, x, y);
        inv = _inv;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return inv.isItemValidForSlot(this.getSlotIndex(), itemStack);
    }
}
