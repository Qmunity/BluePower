package net.quetzi.bluepower.containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.containers.slots.SlotMachineInput;
import net.quetzi.bluepower.containers.slots.SlotMachineOutput;
import net.quetzi.bluepower.tileentities.tier1.TileAlloyFurnace;

public class ContainerAlloyFurnace extends Container
{
    private TileAlloyFurnace tileFurnace;

    private int currentBurnTime;
    private int maxBurnTime;

    public ContainerAlloyFurnace(InventoryPlayer invPlayer, TileAlloyFurnace furnace)
    {
        tileFurnace = furnace;

        addSlotToContainer(new SlotMachineInput(furnace, 9, 21, 35));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotMachineInput(furnace, (i * 3) + j, 47 + (i * 18), 17 + (j * 18)));
            }
        }
        addSlotToContainer(new SlotMachineOutput(furnace, 10, 134, 35));

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

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters)
        {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.currentBurnTime != this.tileFurnace.currentBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileFurnace.currentBurnTime);
            }

            if (this.maxBurnTime != this.tileFurnace.maxBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileFurnace.maxBurnTime);
            }
        }

        this.currentBurnTime = this.tileFurnace.currentBurnTime;
        this.maxBurnTime = this.tileFurnace.maxBurnTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.tileFurnace.currentBurnTime = par2;
        }

        if (par1 == 1)
        {
            this.tileFurnace.maxBurnTime = par2;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return tileFurnace.isUseableByPlayer(entityplayer);
    }


}
