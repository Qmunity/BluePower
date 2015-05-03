package com.bluepowermod.api.misc;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

/**
 * A very simple heat source that checks if the block (and metadata) match with the ones specified and if so, returns a certain amount of heat.
 */
public class SimpleHeatSource implements IHeatSource {

    private Block block;
    private int meta = -1;

    private double heat;

    public SimpleHeatSource(Block block, double heat) {

        this.block = block;
        this.heat = heat;
    }

    public SimpleHeatSource(Block block, int meta, double heat) {

        this(block, heat);
        this.meta = meta;
    }

    @Override
    public double getProducedHeat(IBlockAccess world, int x, int y, int z) {

        if (world.getBlock(x, y, z) != block)
            return 0;

        if (meta >= 0 && world.getBlockMetadata(x, y, z) != meta)
            return 0;

        return heat;
    }

}
