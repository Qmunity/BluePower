package com.bluepowermod.helper;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class LocationCache<CachedType> {
    private final CachedType[] cachedValue;

    public LocationCache(World world, int x, int y, int z, Object... extraArgs) {

        if (world == null)
            throw new NullPointerException("World can't be null!");
        cachedValue = (CachedType[]) new Object[6];
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            cachedValue[d.ordinal()] = getNewValue(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, extraArgs);
        }
    }

    protected abstract CachedType getNewValue(World world, int x, int y, int z, Object... extraArgs);

    public CachedType getValue(ForgeDirection side) {
        return cachedValue[side.ordinal()];
    }
}
