package com.bluepowermod.containers.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

import com.bluepowermod.containers.ContainerProjectTable;
import com.bluepowermod.tileentities.tier1.TileProjectTable;

public class SlotProjectTableCrafting extends SlotCrafting {
    
    /** The craft matrix inventory linked to this result slot. */
    private final IInventory       craftMatrix;
    private final TileProjectTable projectTable;
    
    public SlotProjectTableCrafting(TileProjectTable projectTable, EntityPlayer p_i1823_1_, IInventory craftMatrix, IInventory p_i1823_3_, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_) {
    
        super(p_i1823_1_, craftMatrix, p_i1823_3_, p_i1823_4_, p_i1823_5_, p_i1823_6_);
        this.projectTable = projectTable;
        this.craftMatrix = craftMatrix;
    }
    
    @Override
    public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
    
        ItemStack[] oldGrid = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            oldGrid[i] = craftMatrix.getStackInSlot(i);
        }
        
        super.onPickupFromSlot(p_82870_1_, p_82870_2_);
        
        for (int i = 0; i < 9; i++) {
            if (craftMatrix.getStackInSlot(i) == null && oldGrid[i] != null) {
                oldGrid[i].stackSize = 1;
                ItemStack stackFromTable = ContainerProjectTable.extractStackFromTable(projectTable, oldGrid[i], false);
                craftMatrix.setInventorySlotContents(i, stackFromTable);
            }
        }
        
    }
    
}
