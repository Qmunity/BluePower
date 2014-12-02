package com.bluepowermod.part.wire;

import com.bluepowermod.api.redstone.RedstoneColor;

public class PartWireInfusedTeslatite extends PartWire {

    public PartWireInfusedTeslatite(RedstoneColor bundleColor, Boolean unused) {

        super(bundleColor, unused);
    }

    public PartWireInfusedTeslatite(RedstoneColor insulationColor) {

        super(insulationColor);
    }

    @Override
    public String getType() {

        String type = "wire.infusedteslatite";

        if (isBundled()) {
            type += ".bundled";
            if (bundleColor != RedstoneColor.NONE)
                type += "." + bundleColor.name().toLowerCase();
        } else {
            if (insulationColor != RedstoneColor.NONE)
                type += "." + insulationColor.name().toLowerCase();
        }

        return type;
    }

    @Override
    public boolean isAnalog() {

        return true;
    }

    @Override
    public boolean hasLoss() {

        return false;
    }

    @Override
    public int getColor() {

        return 0xDD00BB;
    }

}
