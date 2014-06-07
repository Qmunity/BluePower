package net.quetzi.bluepower.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMachineOutput extends Slot
{

    public SlotMachineOutput(IInventory inv, int slotNum, int x, int y)
    {
        super(inv, slotNum, x, y);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return false;
    }
}
