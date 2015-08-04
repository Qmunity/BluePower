package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.util.ForgeDirection;

@Deprecated
public interface IRedConductor {

    @Deprecated
    public boolean hasLoss(ForgeDirection side);

    @Deprecated
    public boolean isAnalogue(ForgeDirection side);

}
