package com.bluepowermod.api.wire.redstone;

import net.minecraft.util.EnumFacing;;

public interface IRedConductor {

    public boolean hasLoss(EnumFacing side);

    public boolean isAnalogue(EnumFacing side);

}
