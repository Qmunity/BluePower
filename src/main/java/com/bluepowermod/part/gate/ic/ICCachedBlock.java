package com.bluepowermod.part.gate.ic;

import net.minecraft.world.World;

import com.bluepowermod.util.CachedBlock;

public class ICCachedBlock extends CachedBlock {

    private GateIntegratedCircuit ic;

    public ICCachedBlock(GateIntegratedCircuit ic, int x, int y, int z) {

        super(null, x, y, z);
        this.ic = ic;
    }

    @Override
    public World getWorld() {

        return ICFakeWorld.load(ic);
    }

}
