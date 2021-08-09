/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container.slot;

import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tile.tier2.TileCircuitTable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SlotCircuitTableCrafting extends ResultSlot {

    private final TileCircuitTable circuitTable;


    public SlotCircuitTableCrafting(Player p_i1823_1_, Container circuitTable, CraftingContainer craftSlot, int p_i1823_4_, int p_i1823_5_,
                                    int p_i1823_6_) {

        super(p_i1823_1_, craftSlot, circuitTable, p_i1823_4_, p_i1823_5_, p_i1823_6_);
        this.circuitTable = (TileCircuitTable) circuitTable;
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            return canCraft(stack, circuitTable);
        } else {
            return false;
        }
    }

    public static boolean canCraft(ItemStack stack, TileCircuitTable circuitTable) {

        List<ItemStack> requiredItems = getCraftingComponents(stack);
        if (requiredItems.size() == 0)
            return false;
        for (ItemStack requiredItem : requiredItems) {
            ItemStack extractedStack = IOHelper.extract(circuitTable, null, requiredItem, true, true);
            if (extractedStack.isEmpty() || extractedStack.getCount() < requiredItem.getCount())
                return false;
        }
        return true;
    }

    @Override
    public void onQuickCraft(ItemStack p_75220_1_,  ItemStack craftedItem) {
        //FMLCommonHandler.instance().firePlayerCraftingEvent(player, craftedItem, circuitTable);
        this.checkTakeAchievements(craftedItem);
        List<ItemStack> requiredItems = getCraftingComponents(craftedItem);
        for (ItemStack requiredItem : requiredItems) {
            IOHelper.extract(circuitTable, null, requiredItem, true, false, 1);
        }
        ItemStack item = craftedItem.copy();
        item.setCount(1);
        set(item);
    }


    private static List<ItemStack> getCraftingComponents(ItemStack gate) {

      /* TODO: Update this for 1.14
      List<ItemStack> requiredItems = new ArrayList<ItemStack>();
        for (IRecipe r : CraftingManager.REGISTRY) {
            ItemStack result = r.getResultItem();
            if (!result.isEmpty() && result.sameItem(gate)) {
                if (r instanceof ShapedOreRecipe) {
                    ShapedOreRecipe recipe = (ShapedOreRecipe) r;
                    for (Object o : recipe.getIngredients()) {
                        if (o != null) {
                            ItemStack stack;
                            if (o instanceof ItemStack) {
                                stack = (ItemStack) o;
                            } else {
                                List<ItemStack> list = (List<ItemStack>) o;
                                stack = list.size() > 0 ? list.get(0) : ItemStack.EMPTY;
                            }
                            if (!stack.isEmpty()) {
                                boolean needsAdding = true;
                                for (ItemStack listStack : requiredItems) {
                                    if (listStack.sameItem(stack)) {
                                        listStack.setCount(listStack.getCount() + 1);
                                        needsAdding = false;
                                        break;
                                    }
                                }
                                if (needsAdding)
                                    requiredItems.add(stack.copy());
                            }
                        }
                    }
                    return requiredItems;
                } else if (r instanceof ShapedRecipe) {
                    ShapedRecipe recipe = (ShapedRecipe) r;
                    for (Ingredient stack : recipe.recipeItems) {
                        if (!stack.getItems()[0].isEmpty()) {
                            boolean needsAdding = true;
                            for (ItemStack listStack : requiredItems) {
                                if (listStack.sameItem(stack.getItems()[0])) {
                                    listStack.setCount(listStack.getCount() + 1);
                                    needsAdding = false;
                                    break;
                                }
                            }
                            if (needsAdding)
                                requiredItems.add(stack.getItems()[0]);
                        }
                    }
                    return requiredItems;
                }
            }
        }*/
        return new ArrayList<ItemStack>();
    }
}
