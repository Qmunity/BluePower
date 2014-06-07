package net.quetzi.bluepower.compat;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public abstract class CompatModule
{

    public abstract void preInit(FMLPreInitializationEvent ev);

    public abstract void init(FMLInitializationEvent ev);

    public abstract void postInit(FMLPostInitializationEvent ev);

}
