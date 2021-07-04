/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.jei;
/*

import codechicken.nei.FastTransferManager;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.stack.PositionedStack;
import codechicken.nei.recipe.DefaultOverlayHandler.DistributedIngred;
import codechicken.nei.recipe.DefaultOverlayHandler.IngredientDistribution;
import codechicken.nei.recipe.IRecipeHandler;
import com.bluepowermod.helper.ItemStackHelper;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.*;

*/
/**
 * @author MineMaarten This class is very closely derived from ChickenBones' NEI DefaultOverlayHandler class.
 *//*

public class ProjectTableOverlayHandler implements IOverlayHandler {

    @Override
    public void overlayRecipe(GuiContainer gui, IRecipeHandler recipe, int recipeIndex, boolean shift) {

        List<PositionedStack> ingredients = recipe.getIngredientStacks(recipeIndex);
        List<DistributedIngred> ingredStacks = getPermutationIngredients(ingredients);

        if (!clearIngredients(gui))
            return;

        findInventoryQuantities(gui, ingredStacks);

        List<IngredientDistribution> assignedIngredients = assignIngredients(ingredients, ingredStacks);
        if (assignedIngredients == null)
            return;

        assignIngredSlots(gui, ingredients, assignedIngredients);
        int quantity = calculateRecipeQuantity(assignedIngredients);

        if (quantity != 0)
            moveIngredients(gui, assignedIngredients, 1);
    }

    private int calculateRecipeQuantity(List<IngredientDistribution> assignedIngredients) {

        int quantity = Integer.MAX_VALUE;
        for (IngredientDistribution distrib : assignedIngredients) {
            DistributedIngred istack = distrib.distrib;
            if (istack.numSlots == 0)
                return 0;

            int allSlots = istack.invAmount;
            if (allSlots / istack.numSlots > istack.stack.getMaxStackSize())
                allSlots = istack.numSlots * istack.stack.getMaxStackSize();

            quantity = Math.min(quantity, allSlots / istack.distributed);
        }

        return quantity;
    }

    private List<IngredientDistribution> assignIngredients(List<PositionedStack> ingredients, List<DistributedIngred> ingredStacks) {

        ArrayList<IngredientDistribution> assignedIngredients = new ArrayList<IngredientDistribution>();
        for (PositionedStack posstack : ingredients)// assign what we need and have
        {
            DistributedIngred biggestIngred = null;
            ItemStack permutation = null;
            int biggestSize = 0;
            for (ItemStack pstack : posstack.items) {
                for (int j = 0; j < ingredStacks.size(); j++) {
                    DistributedIngred istack = ingredStacks.get(j);
                    if (!ItemStackHelper.canStack(pstack, istack.stack) || istack.invAmount - istack.distributed < pstack.getCount())
                        continue;

                    int relsize = (istack.invAmount - istack.invAmount / istack.recipeAmount * istack.distributed) / pstack.getCount();
                    if (relsize > biggestSize) {
                        biggestSize = relsize;
                        biggestIngred = istack;
                        permutation = pstack;
                        break;
                    }
                }
            }

            if (biggestIngred == null) // not enough ingreds
                return null;

            biggestIngred.distributed += permutation.getCount();
            assignedIngredients.add(new IngredientDistribution(biggestIngred, permutation));
        }

        return assignedIngredients;
    }

    private List<DistributedIngred> getPermutationIngredients(List<PositionedStack> ingredients) {

        ArrayList<DistributedIngred> ingredStacks = new ArrayList<DistributedIngred>();
        for (PositionedStack posstack : ingredients)// work out what we need
        {
            for (ItemStack pstack : posstack.items) {
                DistributedIngred istack = findIngred(ingredStacks, pstack);
                if (istack == null)
                    ingredStacks.add(istack = new DistributedIngred(pstack));
                istack.recipeAmount += pstack.getCount();
            }
        }
        return ingredStacks;
    }

    private Slot[][] assignIngredSlots(GuiContainer gui, List<PositionedStack> ingredients, List<IngredientDistribution> assignedIngredients) {

        Slot[][] recipeSlots = mapIngredSlots(gui, ingredients);// setup the slot map

        HashMap<Slot, Integer> distribution = new HashMap<Slot, Integer>();
        for (Slot[] recipeSlot : recipeSlots)
            for (Slot slot : recipeSlot)
                if (!distribution.containsKey(slot))
                    distribution.put(slot, -1);

        HashSet<Slot> avaliableSlots = new HashSet<Slot>(distribution.keySet());
        HashSet<Integer> remainingIngreds = new HashSet<Integer>();
        ArrayList<LinkedList<Slot>> assignedSlots = new ArrayList<LinkedList<Slot>>();
        for (int i = 0; i < ingredients.size(); i++) {
            remainingIngreds.add(i);
            assignedSlots.add(new LinkedList<Slot>());
        }

        while (avaliableSlots.size() > 0 && remainingIngreds.size() > 0) {
            for (Iterator<Integer> iterator = remainingIngreds.iterator(); iterator.hasNext();) {
                int i = iterator.next();
                boolean assigned = false;
                DistributedIngred istack = assignedIngredients.get(i).distrib;

                for (Slot slot : recipeSlots[i]) {
                    if (avaliableSlots.contains(slot)) {
                        avaliableSlots.remove(slot);
                        if (slot.hasItem())
                            continue;

                        istack.numSlots++;
                        assignedSlots.get(i).add(slot);
                        assigned = true;
                        break;
                    }
                }

                if (!assigned || istack.numSlots * istack.stack.getMaxStackSize() >= istack.invAmount)
                    iterator.remove();
            }
        }

        for (int i = 0; i < ingredients.size(); i++)
            assignedIngredients.get(i).slots = assignedSlots.get(i).toArray(new Slot[0]);
        return recipeSlots;
    }

    @SuppressWarnings("unchecked")
    private boolean clearIngredients(GuiContainer gui) {

        for (Slot slot : (List<Slot>) gui.inventorySlots.inventorySlots)
            if (slot.inventory instanceof InventoryCrafting) {
                if (!slot.hasItem())
                    continue;
                //todo check this
                FastTransferManager.clickSlot(gui, slot.slotNumber, 0, ClickType.PICKUP);
                if (slot.hasItem())
                    return false;
            }

        return true;
    }

    @SuppressWarnings("unchecked")
    private void findInventoryQuantities(GuiContainer gui, List<DistributedIngred> ingredStacks) {

        for (Slot slot : (List<Slot>) gui.inventorySlots.inventorySlots)// work out how much we have to go round
        {
            if (slot.hasItem() && (slot.inventory instanceof TileProjectTable || slot.inventory instanceof InventoryPlayer)) {
                ItemStack pstack = slot.getItem();
                DistributedIngred istack = findIngred(ingredStacks, pstack);
                if (istack != null)
                    istack.invAmount += pstack.getCount();
            }
        }
    }

    public DistributedIngred findIngred(List<DistributedIngred> ingredStacks, ItemStack pstack) {

        for (DistributedIngred istack : ingredStacks)
            if (ItemStackHelper.canStack(pstack, istack.stack))
                return istack;
        return null;
    }

    @SuppressWarnings("unchecked")
    private void moveIngredients(GuiContainer gui, List<IngredientDistribution> assignedIngredients, int quantity) {

        for (IngredientDistribution distrib : assignedIngredients) {
            ItemStack pstack = distrib.permutation;
            int transferCap = quantity * pstack.getCount();
            int transferred = 0;

            int destSlotIndex = 0;
            Slot dest = distrib.slots[0];
            int slotTransferred = 0;
            int slotTransferCap = pstack.getMaxStackSize();

            for (Slot slot : (List<Slot>) gui.inventorySlots.inventorySlots) {
                if (!slot.hasItem() || !(slot.inventory instanceof TileProjectTable) && !(slot.inventory instanceof InventoryPlayer))
                    continue;

                ItemStack stack = slot.getItem();
                if (!ItemStackHelper.canStack(stack, pstack))
                    continue;

                FastTransferManager.clickSlot(gui, slot.slotNumber);
                int amount = Math.min(transferCap - transferred, stack.getCount());
                for (int c = 0; c < amount; c++) {
                    FastTransferManager.clickSlot(gui, dest.slotNumber, 1);
                    transferred++;
                    slotTransferred++;
                    if (slotTransferred >= slotTransferCap) {
                        destSlotIndex++;
                        if (destSlotIndex == distrib.slots.length) {
                            dest = null;
                            break;
                        }
                        dest = distrib.slots[destSlotIndex];
                        slotTransferred = 0;
                    }
                }
                FastTransferManager.clickSlot(gui, slot.slotNumber);
                if (transferred >= transferCap || dest == null)
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Slot[][] mapIngredSlots(GuiContainer gui, List<PositionedStack> ingredients) {

        Slot[][] recipeSlotList = new Slot[ingredients.size()][];
        for (int i = 0; i < ingredients.size(); i++)// identify slots
        {
            LinkedList<Slot> recipeSlots = new LinkedList<Slot>();
            PositionedStack pstack = ingredients.get(i);
            for (Slot slot : (List<Slot>) gui.inventorySlots.inventorySlots) {
                if (slot.xPos == pstack.relx + 9 && slot.yPos == pstack.rely + 10) {
                    recipeSlots.add(slot);
                    break;
                }
            }
            recipeSlotList[i] = recipeSlots.toArray(new Slot[0]);
        }
        return recipeSlotList;
    }
}
*/
