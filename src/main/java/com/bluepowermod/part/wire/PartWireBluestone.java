package com.bluepowermod.part.wire;

import com.bluepowermod.api.redstone.RedstoneColor;

public class PartWireBluestone extends PartWire {

    public PartWireBluestone(RedstoneColor bundleColor, Boolean unused) {

        super(bundleColor, unused);
    }

    public PartWireBluestone(RedstoneColor insulationColor) {

        super(insulationColor);
    }

    @Override
    public String getType() {

        String type = "wire.bluestone";

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

        return false;
    }

    @Override
    public boolean hasLoss() {

        return false;
    }

    @Override
    public int getColor() {

        return 0x4444DD;
    }

}
