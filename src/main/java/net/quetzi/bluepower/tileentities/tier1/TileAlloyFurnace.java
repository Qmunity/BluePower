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

package net.quetzi.bluepower.tileentities.tier1;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.quetzi.bluepower.api.recipe.IAlloyFurnaceRecipe;
import net.quetzi.bluepower.recipe.AlloyFurnaceRegistry;
import net.quetzi.bluepower.tileentities.TileBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author MineMaarten, Koen Beckers (K-4U)
 */

public class TileAlloyFurnace extends TileBase implements IInventory {
    
    private boolean     isActive;
    public int          currentBurnTime;
    public int          currentProcessTime;
    public int          maxBurnTime;
    private boolean     metaSet = false;
    private ItemStack[] inventory;
    private ItemStack   fuelInventory;
    private ItemStack   outputInventory;
    
    public TileAlloyFurnace() {
    
        inventory = new ItemStack[9];
    }
    
    /*************** BASIC TE FUNCTIONS **************/
    
    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {
    
        super.readFromNBT(tCompound);
        isActive = tCompound.getBoolean("isActive");
        metaSet = false;
        
        for (int i = 0; i < 9; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            inventory[i] = ItemStack.loadItemStackFromNBT(tc);
        }
        fuelInventory = ItemStack.loadItemStackFromNBT(tCompound.getCompoundTag("fuelInventory"));
        outputInventory = ItemStack.loadItemStackFromNBT(tCompound.getCompoundTag("outputInventory"));
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound) {
    
        super.writeToNBT(tCompound);
        tCompound.setBoolean("isActive", isActive);
        
        for (int i = 0; i < 9; i++) {
            if (inventory[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                inventory[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
        if (fuelInventory != null) {
            NBTTagCompound fuelCompound = new NBTTagCompound();
            fuelInventory.writeToNBT(fuelCompound);
            tCompound.setTag("fuelInventory", fuelCompound);
        }
        
        if (outputInventory != null) {
            NBTTagCompound outputCompound = new NBTTagCompound();
            outputInventory.writeToNBT(outputCompound);
            tCompound.setTag("outputInventory", outputCompound);
        }
    }
    
    /**
     * Function gets called every tick. Do not forget to call the super method!
     */
    @Override
    public void updateEntity() {
    
        super.updateEntity();
        
        // Check if the meta is already set after loading the NBT.
        if (!metaSet) {
            metaSet = true;
            if (isActive) {
                int newMeta = getBlockMetadata();
                newMeta = newMeta & 7;
                newMeta |= isActive == true ? 8 : 0;
                getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 2);
            }
        }
        
        if (!worldObj.isRemote) {
            setIsActive(currentBurnTime > 0);
            if (isActive) {
                currentBurnTime--;
            }
            IAlloyFurnaceRecipe recipe = AlloyFurnaceRegistry.getInstance().getMatchingRecipe(inventory, outputInventory);
            if (recipe != null) {
                if (currentBurnTime <= 0) {
                    if (TileEntityFurnace.isItemFuel(fuelInventory)) {
                        // Put new item in
                        currentBurnTime = maxBurnTime = TileEntityFurnace.getItemBurnTime(fuelInventory) + 1;
                        if (fuelInventory != null) {
                            fuelInventory.stackSize--;
                            if (fuelInventory.stackSize <= 0) {
                                fuelInventory = fuelInventory.getItem().getContainerItem(fuelInventory);
                            }
                        }
                    } else {
                        currentProcessTime = 0;
                    }
                }
                
                if (++currentProcessTime >= 200) {
                    currentProcessTime = 0;
                    if (outputInventory != null) {
                        outputInventory.stackSize += recipe.getCraftingResult(inventory).stackSize;
                    } else {
                        outputInventory = recipe.getCraftingResult(inventory).copy();
                    }
                    recipe.useItems(inventory);
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            } else {
                currentProcessTime = 0;
            }
        }
    }
    
    @Override
    protected void redstoneChanged(boolean newValue) {
    
        // setIsActive(newValue);
    }
    
    @SideOnly(Side.CLIENT)
    public void setBurnTicks(int _maxBurnTime, int _currentBurnTime) {
    
        maxBurnTime = _maxBurnTime;
        currentBurnTime = _currentBurnTime;
    }
    
    public float getBurningPercentage() {
    
        if (maxBurnTime > 0) {
            return (float) currentBurnTime / (float) maxBurnTime;
        } else {
            return 0;
        }
    }
    
    public float getProcessPercentage() {
    
        return (float) currentProcessTime / 200;
    }
    
    /**
     * ************* ADDED FUNCTIONS *************
     */
    
    public boolean getIsActive() {
    
        return isActive;
    }
    
    public void setIsActive(boolean _isActive) {
    
        isActive = _isActive;
        int newMeta = getBlockMetadata();
        newMeta = newMeta & 7;
        newMeta |= _isActive == true ? 8 : 0;
        getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newMeta, 2);
    }
    
    /**
     * ************ IINVENTORY ****************
     */
    
    @Override
    public int getSizeInventory() {
    
        return 9 + 1 + 1; // 9 inventory, 1 fuel, 1 output
    }
    
    @Override
    public ItemStack getStackInSlot(int var1) {
    
        if (var1 == 0) {
            return fuelInventory;
        } else if (var1 == 1) {
            return outputInventory;
        } else if (var1 < 11) { return inventory[var1 - 2]; }
        return null;
    }
    
    @Override
    public ItemStack decrStackSize(int var1, int var2) {
    
        ItemStack tInventory = getStackInSlot(var1);
        
        if (tInventory == null) { return null; }
        
        ItemStack ret = null;
        if (tInventory.stackSize < var2) {
            ret = tInventory;
            inventory = null;
        } else {
            ret = tInventory.splitStack(var2);
            if (tInventory.stackSize <= 0) {
                if (var1 == 0) {
                    fuelInventory = null;
                } else if (var1 == 1) {
                    outputInventory = null;
                } else {
                    inventory[var1 - 2] = null;
                }
            }
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        
        return ret;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
    
        return getStackInSlot(var1);
    }
    
    @Override
    public void setInventorySlotContents(int var1, ItemStack itemStack) {
    
        if (var1 == 0) {
            fuelInventory = itemStack;
        } else if (var1 == 1) {
            outputInventory = itemStack;
        } else {
            inventory[var1 - 2] = itemStack;
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public String getInventoryName() {
    
        return null;
    }
    
    @Override
    public boolean hasCustomInventoryName() {
    
        return false;
    }
    
    @Override
    public int getInventoryStackLimit() {
    
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
    
        // Todo: Some fancy code here that detects whether the player is far
        // away
        return true;
    }
    
    @Override
    public void openInventory() {
    
    }
    
    @Override
    public void closeInventory() {
    
    }
    
    @Override
    public boolean isItemValidForSlot(int var1, ItemStack itemStack) {
    
        if (var1 == 0) {
            return TileEntityFurnace.isItemFuel(itemStack);
        } else if (var1 == 1) { // Output slot
            return false;
        } else {
            return true;
        }
    }
    
    @Override
    public List<ItemStack> getDrops() {
    
        List<ItemStack> drops = super.getDrops();
        if (fuelInventory != null) drops.add(fuelInventory);
        if (outputInventory != null) drops.add(outputInventory);
        for (ItemStack stack : inventory)
            if (stack != null) drops.add(stack);
        return drops;
    }
}
