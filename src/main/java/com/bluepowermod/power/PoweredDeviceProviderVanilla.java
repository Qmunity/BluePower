package com.bluepowermod.power;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.api.power.IPoweredDeviceProvider;

public class PoweredDeviceProviderVanilla implements IPoweredDeviceProvider {

    @Override
    public IPowered getPoweredDeviceAt(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IPowered)
            return (IPowered) te;

        return null;
    }

}
