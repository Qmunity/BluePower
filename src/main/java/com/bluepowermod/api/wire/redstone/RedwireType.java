/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;

public enum RedwireType {

    BLUESTONE(false, false, 0x4444CC, "ingotBlueAlloy"), RED_ALLOY(true, true, 0xDD0000, "ingotRedAlloy"), INFUSED_TESLATITE(true, false,
            0xAA00BB, "ingotPurpleAlloy");

    private boolean analogue, loss;
    private int color;
    private String ingotOredictName;

    private RedwireType(boolean analogue, boolean loss, int color, String ingotOredictName) {

        this.analogue = analogue;
        this.loss = loss;
        this.color = color;
        this.ingotOredictName = ingotOredictName;
    }

    public boolean isAnalogue() {

        return analogue;
    }

    public boolean hasLoss() {

        return loss;
    }

    public String getName() {

        return name().toLowerCase().replace("_", "");
    }

    public int getColor() {

        return color;
    }

    public String getIngotOredictName() {

        return ingotOredictName;
    }

    public boolean canConnectTo(RedwireType type) {

        if (type == null)
            return false;

        return type == this;// (this == BLUESTONE) == (type == BLUESTONE);
    }

    public PartInfo getPartInfo(MinecraftColor color, boolean bundled) {

        return PartManager.getPartInfo("wire." + getName()
                + (color != MinecraftColor.NONE && color != null ? "." + color.name().toLowerCase() : "") + (bundled ? ".bundled" : ""));
    }

}
