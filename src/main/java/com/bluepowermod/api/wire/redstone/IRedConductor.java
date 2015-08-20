package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRedConductor {

    public boolean hasLoss(ForgeDirection side);

    public boolean isAnalogue(ForgeDirection side);

}
