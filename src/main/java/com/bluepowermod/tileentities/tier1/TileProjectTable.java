package com.bluepowermod.tileentities.tier1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

import com.bluepowermod.tileentities.TileBase;

public class TileProjectTable extends TileBase implements IInventory {

	//So, we need 3x3 inventories for crafting.
	//and 2x9 for the other inventory.
	//0-8 = crafting
	//9 = output
	//10 - 28 = other inventory
	private ItemStack[] inventories;
	
	
	class LocalInventoryCrafting extends InventoryCrafting {

		public LocalInventoryCrafting() {

			super(new Container() {

				@Override
				public boolean canInteractWith(EntityPlayer var1) {
					return false;
				}

			}, 3, 3);
		}

		public Container eventHandler;
	}

	
	public TileProjectTable(){
		inventories = new ItemStack[28];
	}
	
	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		// TODO Auto-generated method stub
		return false;
	};

}
