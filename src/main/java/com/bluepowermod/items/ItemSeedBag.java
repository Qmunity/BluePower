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
 *     
 *     @author Lumien
 */

package com.bluepowermod.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import com.bluepowermod.BluePower;
import com.bluepowermod.containers.inventorys.InventoryItem;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;

public class ItemSeedBag extends ItemBase {
    
    public ItemSeedBag(String name) {
    
        this.setCreativeTab(CustomTabs.tabBluePowerItems);
        this.setUnlocalizedName(name);
        this.setTextureName(Refs.MODID + ":" + name);
    }
    
    public static ItemStack getSeedType(ItemStack seedBag)
    {
        ItemStack seed = null;
        
        IInventory seedBagInventory = InventoryItem.getItemInventory(seedBag, "Seed Bag", 9);
        seedBagInventory.openInventory();
        for (int i = 0; i < seedBagInventory.getSizeInventory(); i++) {
            ItemStack is = seedBagInventory.getStackInSlot(i);
            if (is != null) {
                seed = is;
            }
        }
        
        return seed;
    }
    
    public double getDurabilityForDisplay(ItemStack stack) {
    
        return 1D - (double) getItemDamageForDisplay(stack) / (double) 576;
    }
    
    public boolean showDurabilityBar(ItemStack stack) {
    
        return stack.stackTagCompound!=null;
    }
    
    public int getItemDamageForDisplay(ItemStack stack) {
    
        int items = 0;
        IInventory seedBagInventory = InventoryItem.getItemInventory(stack, "Seed Bag", 9);
        seedBagInventory.openInventory();
        for (int i = 0; i < seedBagInventory.getSizeInventory(); i++) {
            ItemStack is = seedBagInventory.getStackInSlot(i);
            if (is != null) {
                items += is.stackSize;
            }
        }
        return items;
    }
    
    public int getMaxDamage(ItemStack stack) {
    
        return 576;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World worldObj, EntityPlayer playerEntity) {
    
        if (!worldObj.isRemote && playerEntity.isSneaking()) {
            playerEntity.openGui(BluePower.instance, GuiIDs.SEEDBAG.ordinal(), worldObj, (int) playerEntity.posX, (int) playerEntity.posY,
                    (int) playerEntity.posZ);
        }
        return itemstack;
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int posX, int posY, int posZ, int par7,
            float par8, float par9, float par10) {
    
        if (par2EntityPlayer.isSneaking()) { return false; }
        
        IInventory seedBagInventory = InventoryItem.getItemInventory(par2EntityPlayer,par2EntityPlayer.getCurrentEquippedItem(), "Seed Bag", 9);
        seedBagInventory.openInventory();
        
        ItemStack seed = getSeedType(par1ItemStack);
        if (seed != null && seed.getItem() instanceof IPlantable) {
            IPlantable plant = (IPlantable) seed.getItem();
            for (int modX = -2; modX < 3; modX++) {
                for (int modZ = -2; modZ < 3; modZ++) {
                    Block b = par3World.getBlock(posX + modX, posY, posZ + modZ);
                    if (b.canSustainPlant(par3World, posX, posY, posZ, ForgeDirection.UP, plant)
                            && par3World.isAirBlock(posX + modX, posY + 1, posZ + modZ)) {
                        for (int i = 0; i < seedBagInventory.getSizeInventory(); i++) {
                            ItemStack is = seedBagInventory.getStackInSlot(i);
                            if (is != null) {
                                seedBagInventory.decrStackSize(i, 1);
                                par3World.setBlock(posX + modX, posY + 1, posZ + modZ, plant.getPlant(par3World, posX + modX, posY + 1, posZ + modZ),
                                        plant.getPlantMetadata(par3World, posX + modX, posY + 1, modZ + modZ), 0x3);
                                break;
                            }
                        }
                    }
                }
            }
            return true;
            
        }
        
        seedBagInventory.closeInventory();
        
        return false;
    }
}
