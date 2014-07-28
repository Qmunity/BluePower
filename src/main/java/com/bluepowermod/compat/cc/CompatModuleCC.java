package com.bluepowermod.compat.cc;

import com.bluepowermod.compat.CompatModule;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dan200.computercraft.api.ComputerCraftAPI;

public class CompatModuleCC extends CompatModule{

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        ComputerCraftAPI.registerPeripheralProvider(PeripheralProvider.INSTANCE);
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
    public void registerRenders() {

    }
}
