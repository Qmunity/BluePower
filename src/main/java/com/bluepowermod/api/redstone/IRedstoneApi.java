package com.bluepowermod.api.redstone;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstoneApi {

    public byte[] getBundledOutput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    public void registerRedstoneProvider(IRedstoneProvider provider);

    public IPropagator getPropagator();

}
