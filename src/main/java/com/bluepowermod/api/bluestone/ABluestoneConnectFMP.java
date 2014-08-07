package com.bluepowermod.api.bluestone;

import net.minecraftforge.common.util.ForgeDirection;
import codechicken.multipart.TMultiPart;

import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.part.cable.bluestone.WireBluestone;

public abstract class ABluestoneConnectFMP extends ABluestoneConnect {

    @Override
    public final int getExtraLength(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide) {

        TMultiPart p = WireBluestone.getFMPPartOnSide(wire, cableSide);
        if (p == null)
            return 0;

        return getExtraLength(p, wire, cableSide);
    }

    @Override
    public final boolean canConnect(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide) {

        TMultiPart p = WireBluestone.getFMPPartOnSide(wire, cableSide);
        if (p == null)
            return false;

        return canConnect(p, wire, cableSide);
    }

    @Override
    public final void renderExtraCables(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide) {

        TMultiPart p = WireBluestone.getFMPPartOnSide(wire, cableSide);
        if (p == null)
            return;

        renderExtraCables(p, wire, cableSide);
    }

    public abstract int getExtraLength(TMultiPart part, IBluestoneWire wire, ForgeDirection cableSide);

    public abstract boolean canConnect(TMultiPart part, IBluestoneWire wire, ForgeDirection cableSide);

    public abstract void renderExtraCables(TMultiPart part, IBluestoneWire wire, ForgeDirection cableSide);

}
