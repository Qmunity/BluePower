package net.quetzi.bluepower.compat.cc;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quetzi.bluepower.compat.CompatModule;

public class CompatModuleCC extends CompatModule implements IPeripheralProvider {

    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile instanceof IPeripheral)
            return (IPeripheral) tile;
        return null;
    }

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        ComputerCraftAPI.registerPeripheralProvider(this);
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
