package com.bluepowermod.recipe;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.init.BPBlocks;

public class RecipeCastPlate implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World w) {

        return getCraftingResult(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        ItemStack template = null;
        boolean clay = false;
        boolean plate = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack is = inv.getStackInSlot(i);
            if (is == null)
                continue;
            if (is.getItem() instanceof ItemBlock && Block.getBlockFromItem(is.getItem()) == Blocks.clay) {
                if (clay)
                    return null;
                clay = true;
                continue;
            }
            if (is.getItem() instanceof ItemBlock && Block.getBlockFromItem(is.getItem()) == BPBlocks.cast_plate) {
                if (plate)
                    return null;
                plate = true;
                continue;
            }
            if (BPApi.getInstance().getCastRegistry().getCreatedCast(is) != null) {
                if (template != null)
                    return null;
                template = is.copy();
                template.stackSize = 1;
                continue;
            }
            return null;
        }

        if (template == null || !clay || !plate)
            return null;

        NBTTagCompound tag = new NBTTagCompound();
        // Template
        {
            NBTTagCompound inventory0 = new NBTTagCompound();
            template.writeToNBT(inventory0);
            tag.setTag("inventory0", inventory0);
        }
        tag.setBoolean("hasClay", true);

        ItemStack is = new ItemStack(BPBlocks.cast_plate);
        is.stackTagCompound = tag;

        return is;
    }

    @Override
    public int getRecipeSize() {

        return 3;
    }

    @Override
    public ItemStack getRecipeOutput() {

        return null;
    }

}
