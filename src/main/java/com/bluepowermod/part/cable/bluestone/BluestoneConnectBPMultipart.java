package com.bluepowermod.part.cable.bluestone;

import net.minecraftforge.common.util.ForgeDirection;
import codechicken.multipart.TMultiPart;

import com.bluepowermod.api.bluestone.ABluestoneConnectFMP;
import com.bluepowermod.api.bluestone.IBluestoneWire;
import com.bluepowermod.compat.fmp.MultipartBPPart;
import com.bluepowermod.part.lamp.PartLamp;

public class BluestoneConnectBPMultipart extends ABluestoneConnectFMP {

    @Override
    public int getExtraLength(TMultiPart part, IBluestoneWire wire, ForgeDirection cableSide) {

        if (part instanceof MultipartBPPart) {
            MultipartBPPart p = (MultipartBPPart) part;
            if (p.getPart() instanceof PartLamp) {
                return 8;
            }
        }

        return 0;
    }

    @Override
    public boolean canConnect(TMultiPart part, IBluestoneWire wire, ForgeDirection cableSide) {

        return false;
    }

    @Override
    public void renderExtraCables(TMultiPart part, IBluestoneWire wire, ForgeDirection cableSide) {

    }

}
