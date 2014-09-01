/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
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
