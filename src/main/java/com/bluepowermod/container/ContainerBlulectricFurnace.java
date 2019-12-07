package com.bluepowermod.container;

import com.bluepowermod.container.slot.SlotMachineInput;
import com.bluepowermod.container.slot.SlotMachineOutput;
import com.bluepowermod.tile.tier3.TileBlulectricFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBlulectricFurnace extends Container {

    private final TileBlulectricFurnace tileFurnace;

    private int                    currentEnergy;
    private int                    maxEnergy;
    private int                    currentProcessTime;

    public ContainerBlulectricFurnace(InventoryPlayer invPlayer, TileBlulectricFurnace furnace) {

        tileFurnace = furnace;

        addSlotToContainer(new SlotMachineInput(furnace, 0, 62, 35));
        addSlotToContainer(new SlotMachineOutput(furnace, 1, 126, 35));
        bindPlayerInventory(invPlayer);

    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            stack1 = stack.copy();

            if (index < 2) {
                if (!mergeItemStack(stack, 2, 37, false))
                    return ItemStack.EMPTY;
                slot.onSlotChange(stack, stack1);
            } else {
                if (!mergeItemStack(stack, 0, 1, false))
                    return ItemStack.EMPTY;
                slot.onSlotChange(stack, stack1);
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == stack1.getCount())
                return ItemStack.EMPTY;

            slot.onTake(playerIn, stack);
        }

        return stack1;
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : listeners) {
            IContainerListener icrafting = (IContainerListener) crafter;

            if (currentEnergy != tileFurnace.storage.getEnergy()){
                icrafting.sendWindowProperty(this, 0, (int) tileFurnace.storage.getEnergy());
            }

            if (maxEnergy != tileFurnace.storage.getMaxEnergy()){
                icrafting.sendWindowProperty(this, 1, (int) tileFurnace.storage.getMaxEnergy());
            }

            if (currentProcessTime != tileFurnace.currentProcessTime) {
                icrafting.sendWindowProperty(this, 2, tileFurnace.currentProcessTime);
            }
        }

        currentEnergy = (int) tileFurnace.storage.getEnergy();
        maxEnergy = (int) tileFurnace.storage.getMaxEnergy();
        currentProcessTime = tileFurnace.currentProcessTime;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {

        if (par1 == 2) {
            tileFurnace.currentProcessTime = par2;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {

        return tileFurnace.isUsableByPlayer(entityplayer);
    }
}
