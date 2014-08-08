package com.bluepowermod.compat.hydcraft;

import com.bluepowermod.compat.CompatModule;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public class CompatModuleHydCraft extends CompatModule {
    
    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        NBTTagCompound toRegister = new NBTTagCompound();
        ItemStack beginStack = new ItemStack(BPBlocks.zinc_ore, 1);
        ItemStack endStack = new ItemStack(BPItems.zinc_ore_crushed, 2);
        NBTTagCompound itemFrom = new NBTTagCompound();
        NBTTagCompound itemTo = new NBTTagCompound();

        beginStack.writeToNBT(itemFrom);
        endStack.writeToNBT(itemTo);

        toRegister.setTag("itemFrom", itemFrom);
        toRegister.setTag("itemTo", itemTo);
        toRegister.setFloat("pressureRatio", 1.0F);
        FMLInterModComms.sendMessage("HydCraft", "registerCrushingRecipe", toRegister);

        toRegister = new NBTTagCompound();
        beginStack = new ItemStack(BPItems.zinc_ingot, 1);
        endStack = new ItemStack(BPItems.zinc_dust, 1);
        itemFrom = new NBTTagCompound();
        itemTo = new NBTTagCompound();

        beginStack.writeToNBT(itemFrom);
        endStack.writeToNBT(itemTo);

        toRegister.setTag("itemFrom", itemFrom);
        toRegister.setTag("itemTo", itemTo);
        toRegister.setFloat("pressureRatio", 1.0F);
        FMLInterModComms.sendMessage("HydCraft", "registerCrushingRecipe", toRegister);

        toRegister = new NBTTagCompound();
        beginStack = new ItemStack(BPItems.zinc_ore_crushed, 1);
        endStack = new ItemStack(BPItems.zinc_dust, 1);
        itemFrom = new NBTTagCompound();
        itemTo = new NBTTagCompound();

        beginStack.writeToNBT(itemFrom);
        endStack.writeToNBT(itemTo);

        toRegister.setTag("itemFrom", itemFrom);
        toRegister.setTag("itemTo", itemTo);
        toRegister.setFloat("pressureRatio", 1.0F);
        FMLInterModComms.sendMessage("HydCraft", "registerWashingRecipe", toRegister);


    }
    
    @Override
    public void init(FMLInitializationEvent ev) {
    
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void postInit(FMLPostInitializationEvent ev) {
    
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void registerBlocks() {
    
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void registerItems() {
    
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void registerRenders() {
    
        // TODO Auto-generated method stub
        
    }
    
}
