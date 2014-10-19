package com.bluepowermod.helper;

import net.minecraft.world.World;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.compat.MultipartCompatibility;

public class PartCache<CachedPart extends IPart> extends LocationCache<CachedPart> {
    public <T> PartCache(World world, int x, int y, int z, Class<? extends IPart> searchedParts) {
        super(world, x, y, z, searchedParts);
    }

    @Override
    protected CachedPart getNewValue(World world, int x, int y, int z, Object... extraArgs) {
        return (CachedPart) MultipartCompatibility.getPart(world, x, y, z, (Class<? extends IPart>) extraArgs[0]);
    }

}
