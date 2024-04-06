/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.hydcraft;

import com.bluepowermod.compat.CompatModule;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
/*
public class CompatModuleHydCraft extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        CompoundTag toRegister = new CompoundTag();
        ItemStack beginStack = new ItemStack(BPBlocks.zinc_ore, 1);
        ItemStack endStack = new ItemStack(BPItems.zinc_ore_crushed, 2);
        CompoundTag itemFrom = new CompoundTag();
        CompoundTag itemTo = new CompoundTag();

        beginStack.writeToNBT(itemFrom);
        endStack.writeToNBT(itemTo);

        toRegister.setTag("itemFrom", itemFrom);
        toRegister.setTag("itemTo", itemTo);
        toRegister.setFloat("pressureRatio", 1.0F);
        FMLInterModComms.sendMessage("HydCraft", "registerCrushingRecipe", toRegister);

        toRegister = new CompoundTag();
        beginStack = new ItemStack(BPItems.zinc_ingot, 1);
        endStack = new ItemStack(BPItems.zinc_dust, 1);
        itemFrom = new CompoundTag();
        itemTo = new CompoundTag();

        beginStack.writeToNBT(itemFrom);
        endStack.writeToNBT(itemTo);

        toRegister.setTag("itemFrom", itemFrom);
        toRegister.setTag("itemTo", itemTo);
        toRegister.setFloat("pressureRatio", 1.0F);
        FMLInterModComms.sendMessage("HydCraft", "registerCrushingRecipe", toRegister);

        toRegister = new CompoundTag();
        beginStack = new ItemStack(BPItems.zinc_ore_crushed, 1);
        endStack = new ItemStack(BPItems.zinc_dust, 1);
        itemFrom = new CompoundTag();
        itemTo = new CompoundTag();

        beginStack.writeToNBT(itemFrom);
        endStack.writeToNBT(itemTo);

        toRegister.setTag("itemFrom", itemFrom);
        toRegister.setTag("itemTo", itemTo);
        toRegister.setFloat("pressureRatio", 1.0F);
        FMLInterModComms.sendMessage("HydCraft", "registerWashingRecipe", toRegister);

    }

    @Override
    public void init(FMLInitializationEvent ev) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerRenders() {

    }

}*/
