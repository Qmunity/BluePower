package com.bluepowermod.api.gate;

import net.minecraft.world.level.Level;

public interface IAnalogueComparatorReadout {

    public boolean hasAnalogueComparatorInputOverride();

    public byte getAnalogueComparatorInputOverride(Level world, int x, int y, int z, int side);

}
