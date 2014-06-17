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

package net.quetzi.bluepower.recipe;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.references.Refs;

public class CanvasBagRecipe extends ShapelessOreRecipe {
    
    public CanvasBagRecipe() {
    
        super(BPItems.canvas_bag, new ItemStack(BPItems.canvas_bag, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.dye, 1,
                OreDictionary.WILDCARD_VALUE));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(InventoryCrafting var1, World world) {
    
        ArrayList<Object> required = new ArrayList<Object>(this.getInput());
        
        for (int x = 0; x < var1.getSizeInventory(); x++) {
            ItemStack slot = var1.getStackInSlot(x);
            
            if (slot != null) {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();
                
                while (req.hasNext()) {
                    boolean match = false;
                    
                    Object next = req.next();
                    
                    if (next instanceof ItemStack) {
                        int[] ids = OreDictionary.getOreIDs(slot);
                        for (int id : ids) {
                            String name = OreDictionary.getOreName(id);
                            for (int d = 0; d < Refs.oreDictDyes.length; d++) {
                                String dyeName = Refs.oreDictDyes[d];
                                if (name.equals(dyeName)) {
                                    match = true;
                                }
                            }
                        }
                        if (!match) {
                            match = OreDictionary.itemMatches((ItemStack) next, slot, false);
                        }
                    } else if (next instanceof ArrayList) {
                        Iterator<ItemStack> itr = ((ArrayList<ItemStack>) next).iterator();
                        while (itr.hasNext() && !match) {
                            int[] ids = OreDictionary.getOreIDs(slot);
                            for (int id : ids) {
                                String name = OreDictionary.getOreName(id);
                                for (int d = 0; d < Refs.oreDictDyes.length; d++) {
                                    String dyeName = Refs.oreDictDyes[d];
                                    if (name.equals(dyeName)) {
                                        match = true;
                                    }
                                }
                            }
                            if (!match) {
                                match = OreDictionary.itemMatches(itr.next(), slot, false);
                            }
                        }
                    }
                    
                    if (match) {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }
                
                if (!inRecipe) { return false; }
            }
        }
        
        return required.isEmpty();
    }
    
    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
    
        ItemStack dye = null;
        ItemStack canvasBag = null;
        for (int i = 0; i < var1.getSizeInventory(); i++) {
            ItemStack is = var1.getStackInSlot(i);
            if (is != null) {
                if (is.getItem() == BPItems.canvas_bag) {
                    canvasBag = is;
                } else if (is != null) {
                    dye = is;
                }
            }
        }
        
        if (dye != null) {
            for (int i = 0; i < Refs.oreDictDyes.length; i++) {
                int[] ids = OreDictionary.getOreIDs(dye);
                for (int id : ids) {
                    String name = OreDictionary.getOreName(id);
                    for (int d = 0; d < Refs.oreDictDyes.length; d++) {
                        String dyeName = Refs.oreDictDyes[d];
                        if (name.equals(dyeName) && !(d == 15 - canvasBag.getItemDamage())) {
                            ItemStack output = canvasBag.copy();
                            output.setItemDamage(15 - d);
                            return output;
                        }
                    }
                }
            }
        }
        
        return null;
    }
}
