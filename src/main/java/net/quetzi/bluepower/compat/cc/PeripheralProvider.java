package net.quetzi.bluepower.compat.cc;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PeripheralProvider implements IPeripheralProvider {

    public static final PeripheralProvider INSTANCE = new PeripheralProvider();

    private PeripheralProvider() {

    }
    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile instanceof IPeripheral)
            return (IPeripheral) tile;
        return null;
    }
}
