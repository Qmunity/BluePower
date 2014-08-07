package com.bluepowermod.api.bluestone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.vec.Vector3;

public abstract class ABluestoneConnect {

    public abstract int getExtraLength(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide);

    public abstract boolean canConnect(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide);

    public abstract void renderExtraCables(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide);

}
