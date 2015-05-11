package com.bluepowermod.container;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.container.slot.SlotMachineInput;
import com.bluepowermod.tile.tier2.TileBattery;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.client.gui.GuiContainerBase;

/**!
 * @author Koen Beckers (K4Unl)
 */

public class ContainerBattery extends Container {

    private final TileBattery tileBattery;
    private final static int AMPSTORED = 0;
    private final static int AMPMAX = 1;
    public float ampStored;
    public float ampMax;

    public ContainerBattery(InventoryPlayer invPlayer, TileBattery battery) {

        tileBattery = battery;

        addSlotToContainer(new SlotMachineInput(battery, 0, 120, 27));
        addSlotToContainer(new SlotMachineInput(battery, 1, 120, 55));

        bindPlayerInventory(invPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 88 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 146));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {

        return tileBattery.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {

        //TODO: A check that checks if it's IChargable
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (par2 < 2) {
                if (!mergeItemStack(itemstack1, 2, 37, true)) return null;
            } else if (!mergeItemStack(itemstack1, 0, 2, false)) { return null; }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize != itemstack.stackSize) {
                slot.onPickupFromSlot(player, itemstack1);
            } else {
                return null;
            }
        }
        return itemstack;
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();

        for (Object crafter : crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (ampStored != tileBattery.getHandler().getAmpHourStored()) {
                icrafting.sendProgressBarUpdate(this, AMPSTORED, (int)tileBattery.getHandler().getAmpHourStored());
            }

            if (ampMax != tileBattery.getHandler().getMaxAmpHour()) {
                icrafting.sendProgressBarUpdate(this, AMPMAX, (int)tileBattery.getHandler().getMaxAmpHour());
            }

        }

        ampStored = tileBattery.getHandler().getAmpHourStored();
        ampMax = tileBattery.getHandler().getMaxAmpHour();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {

        if (index == AMPSTORED) {
            tileBattery.setAmpStored(value);
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
        if(index == AMPMAX){
            tileBattery.setMaxAmp(value);
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }

    }
}
