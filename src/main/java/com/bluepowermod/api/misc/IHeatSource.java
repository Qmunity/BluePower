package com.bluepowermod.api.misc;

import net.minecraft.world.IBlockAccess;

/**
 * Interface implemented by heat sources. You can implement it in your block or your TileEntity.<br/>
 * If you just want very simple heat handling you can use {@link SimpleHeatSource} instead.
 */
public interface IHeatSource {

    public double getProducedHeat(IBlockAccess world, int x, int y, int z);

}
