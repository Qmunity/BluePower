package com.bluepowermod.compat.ic2;

import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.compat.CompatModule;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;

public class CompatModuleIC2 extends CompatModule {
    
    @Override
    public void preInit(FMLPreInitializationEvent ev) {
    
    }
    
    @Override
    public void init(FMLInitializationEvent ev) {
    
        Recipes.macerator.addRecipe(new IC2RecipeInput(new ItemStack(BPBlocks.zinc_ore)), null, new ItemStack(BPItems.zinc_ore_crushed));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("amount", 1000);
        Recipes.oreWashing.addRecipe(new IC2RecipeInput(new ItemStack(BPItems.zinc_ore_crushed)), tag, new ItemStack(BPItems.zinc_ore_crushed_purified), new ItemStack(BPItems.zinc_dust_tiny, 2), new ItemStack(GameData.getItemRegistry().getObject("IC2:itemDust"), 1, 9));
        
        tag = new NBTTagCompound();
        tag.setInteger("minHeat", 2000);
        Recipes.centrifuge.addRecipe(new IC2RecipeInput(new ItemStack(BPItems.zinc_ore_crushed_purified)), tag, new ItemStack(BPItems.zinc_dust), new ItemStack(GameData.getItemRegistry().getObject("IC2:itemDustSmall"), 1, 6));
        Recipes.centrifuge.addRecipe(new IC2RecipeInput(new ItemStack(BPItems.zinc_ore_crushed)), tag, new ItemStack(BPItems.zinc_dust), new ItemStack(GameData.getItemRegistry().getObject("IC2:itemDustSmall"), 1, 6), new ItemStack(GameData.getItemRegistry().getObject("IC2:itemDust"), 1, 9));
        
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
    public void registerRenders() {
    
    }
    
}
