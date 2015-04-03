package com.bluepowermod.api.gate;

import net.minecraft.world.World;

public interface IAnalogueComparatorReadout {

    public boolean hasAnalogueComparatorInputOverride();

    public byte getAnalogueComparatorInputOverride(World world, int x, int y, int z, int side);

}
