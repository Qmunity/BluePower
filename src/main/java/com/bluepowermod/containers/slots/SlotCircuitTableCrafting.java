package com.bluepowermod.containers.slots;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tileentities.tier2.TileCircuitTable;

import cpw.mods.fml.common.FMLCommonHandler;

public class SlotCircuitTableCrafting extends SlotCrafting {
    
    private final TileCircuitTable circuitTable;
    
    public SlotCircuitTableCrafting(EntityPlayer p_i1823_1_, IInventory circuitTable, IInventory craftSlot, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_) {
    
        super(p_i1823_1_, circuitTable, craftSlot, p_i1823_4_, p_i1823_5_, p_i1823_6_);
        this.circuitTable = (TileCircuitTable) circuitTable;
    }
    
    @Override
    public boolean canTakeStack(EntityPlayer p_82869_1_) {
    
        ItemStack stack = getStack();
        if (stack != null) {
            return canCraft(stack, circuitTable);
        } else {
            return false;
        }
    }
    
    @Override
    public void onSlotChanged() {
    
        circuitTable.updateGateInventory();
    }
    
    public static boolean canCraft(ItemStack stack, TileCircuitTable circuitTable) {
    
        List<ItemStack> requiredItems = getCraftingComponents(stack);
        if (requiredItems.size() == 0) return false;
        for (ItemStack requiredItem : requiredItems) {
            ItemStack extractedStack = IOHelper.extract(circuitTable, ForgeDirection.UNKNOWN, requiredItem, true, true);
            if (extractedStack == null || extractedStack.stackSize < requiredItem.stackSize) return false;
        }
        return true;
    }
    
    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack craftedItem) {
    
        FMLCommonHandler.instance().firePlayerCraftingEvent(player, craftedItem, circuitTable);
        this.onCrafting(craftedItem);
        List<ItemStack> requiredItems = getCraftingComponents(craftedItem);
        for (ItemStack requiredItem : requiredItems) {
            IOHelper.extract(circuitTable, ForgeDirection.UNKNOWN, requiredItem, true, false);
        }
    }
    
    private static List<ItemStack> getCraftingComponents(ItemStack gate) {
    
        List<ItemStack> requiredItems = new ArrayList<ItemStack>();
        List recipeList = CraftingManager.getInstance().getRecipeList();
        for (IRecipe r : (List<IRecipe>) recipeList) {
            ItemStack result = r.getRecipeOutput();
            if (ItemStack.areItemStackTagsEqual(result, gate)) {
                if (r instanceof ShapedOreRecipe) {
                    ShapedOreRecipe recipe = (ShapedOreRecipe) r;
                    for (Object o : recipe.getInput()) {
                        if (o != null) {
                            ItemStack stack = (ItemStack) o;
                            boolean needsAdding = true;
                            for (ItemStack listStack : requiredItems) {
                                if (listStack.isItemEqual(stack)) {
                                    listStack.stackSize++;
                                    needsAdding = false;
                                    break;
                                }
                            }
                            if (needsAdding) requiredItems.add(stack.copy());
                        }
                    }
                    return requiredItems;
                } else if (r instanceof ShapedRecipes) {
                    ShapedRecipes recipe = (ShapedRecipes) r;
                    for (ItemStack stack : recipe.recipeItems) {
                        boolean needsAdding = true;
                        for (ItemStack listStack : requiredItems) {
                            if (listStack.isItemEqual(stack)) {
                                listStack.stackSize++;
                                needsAdding = false;
                                break;
                            }
                        }
                        if (needsAdding) requiredItems.add(stack.copy());
                    }
                    return requiredItems;
                }
            }
        }
        return new ArrayList<ItemStack>();
    }
}
