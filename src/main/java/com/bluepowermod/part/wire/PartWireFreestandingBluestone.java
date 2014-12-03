package com.bluepowermod.part.wire;

import com.bluepowermod.api.redstone.RedstoneColor;

public class PartWireFreestandingBluestone extends PartWireFreestanding {

    public PartWireFreestandingBluestone(RedstoneColor bundleColor, Boolean unused) {

        super(bundleColor, unused);
    }

    public PartWireFreestandingBluestone(RedstoneColor insulationColor) {

        super(insulationColor);
    }

    @Override
    public String getType() {

        String type = "wire.freestanding.bluestone";

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
