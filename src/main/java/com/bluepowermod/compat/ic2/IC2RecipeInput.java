/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.ic2;

import ic2.api.recipe.IRecipeInput;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

public class IC2RecipeInput implements IRecipeInput{
    private final ItemStack input;

    public IC2RecipeInput(ItemStack input){
        this.input = input;
    }

    @Override
    public boolean matches(ItemStack subject){
        return subject != null && input.isItemEqual(subject);
    }

    @Override
    public int getAmount(){
        return input.stackSize;
    }

    @Override
    public List<ItemStack> getInputs(){
        return Arrays.asList(new ItemStack[]{input});
    }

}
