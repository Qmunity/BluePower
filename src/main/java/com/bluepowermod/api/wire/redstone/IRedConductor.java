package com.bluepowermod.api.wire.redstone;

import net.minecraft.core.Direction;

public interface IRedConductor {

    public boolean hasLoss(Direction side);

    public boolean isAnalogue(Direction side);

}
