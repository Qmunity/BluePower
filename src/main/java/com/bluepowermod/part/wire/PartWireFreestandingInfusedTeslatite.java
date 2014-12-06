package com.bluepowermod.part.wire;

import com.bluepowermod.api.redstone.RedstoneColor;

public class PartWireFreestandingInfusedTeslatite extends PartWireFreestanding {

    public PartWireFreestandingInfusedTeslatite(RedstoneColor bundleColor, Boolean unused) {

        super(bundleColor, unused);
    }

    public PartWireFreestandingInfusedTeslatite(RedstoneColor insulationColor) {

        super(insulationColor);
    }

    @Override
    public String getType() {

        String type = "wire.freestanding.infusedteslatite";

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

        return 0xAA00BB;
    }

}
