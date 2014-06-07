package net.quetzi.bluepower.compat.fmp;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.quetzi.bluepower.compat.CompatModule;

public class CompatModuleFMP extends CompatModule
{

    @Override
    public void preInit(FMLPreInitializationEvent ev)
    {

    }

    @Override
    public void init(FMLInitializationEvent ev)
    {

        RegisterMultiparts.register();
    }

    @Override
    public void postInit(FMLPostInitializationEvent ev)
    {

    }

}
