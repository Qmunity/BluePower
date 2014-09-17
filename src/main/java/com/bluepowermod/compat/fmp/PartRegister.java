/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.fmp;

import com.bluepowermod.part.cable.bluepower.WireBluePower;
import net.minecraft.item.ItemDye;

import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.part.cable.bluestone.WireBluestone;
import com.bluepowermod.part.gate.GateAnd;
import com.bluepowermod.part.gate.GateBuffer;
import com.bluepowermod.part.gate.GateCounter;
import com.bluepowermod.part.gate.GateLightCell;
import com.bluepowermod.part.gate.GateMux;
import com.bluepowermod.part.gate.GateNand;
import com.bluepowermod.part.gate.GateNor;
import com.bluepowermod.part.gate.GateNot;
import com.bluepowermod.part.gate.GateOr;
import com.bluepowermod.part.gate.GatePulseFormer;
import com.bluepowermod.part.gate.GateRSLatch;
import com.bluepowermod.part.gate.GateRandomizer;
import com.bluepowermod.part.gate.GateRepeater;
import com.bluepowermod.part.gate.GateSequencer;
import com.bluepowermod.part.gate.GateStateCell;
import com.bluepowermod.part.gate.GateSynchronizer;
import com.bluepowermod.part.gate.GateTimer;
import com.bluepowermod.part.gate.GateToggleLatch;
import com.bluepowermod.part.gate.GateTransparentLatch;
import com.bluepowermod.part.gate.GateXnor;
import com.bluepowermod.part.gate.GateXor;
import com.bluepowermod.part.gate.ic.Circuit3x3;
import com.bluepowermod.part.gate.ic.Circuit5x5;
import com.bluepowermod.part.gate.ic.Circuit7x7;
import com.bluepowermod.part.lamp.PartCageLamp;
import com.bluepowermod.part.lamp.PartFixture;
import com.bluepowermod.part.tube.Accelerator;
import com.bluepowermod.part.tube.MagTube;
import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.part.tube.PneumaticTubeOpaque;
import com.bluepowermod.part.tube.RestrictionTube;
import com.bluepowermod.part.tube.RestrictionTubeOpaque;

public class PartRegister {

    public static void registerParts() {

        PartRegistry.getInstance().ICON_PART = "timer";
        // Gates
        PartRegistry.getInstance().registerPart(GateNot.class);
        PartRegistry.getInstance().registerPart(GateAnd.class);
        PartRegistry.getInstance().registerPart(GateTimer.class);
        PartRegistry.getInstance().registerPart(GateSequencer.class);
        PartRegistry.getInstance().registerPart(GateBuffer.class);
        PartRegistry.getInstance().registerPart(GateCounter.class);
        PartRegistry.getInstance().registerPart(GateMux.class);
        PartRegistry.getInstance().registerPart(GateNand.class);
        PartRegistry.getInstance().registerPart(GateOr.class);
        PartRegistry.getInstance().registerPart(GateNor.class);
        PartRegistry.getInstance().registerPart(GatePulseFormer.class);
        PartRegistry.getInstance().registerPart(GateRandomizer.class);
        PartRegistry.getInstance().registerPart(GateLightCell.class);
        PartRegistry.getInstance().registerPart(GateToggleLatch.class);
        PartRegistry.getInstance().registerPart(GateRSLatch.class);
        PartRegistry.getInstance().registerPart(GateXor.class);
        PartRegistry.getInstance().registerPart(GateXnor.class);
        PartRegistry.getInstance().registerPart(GateStateCell.class);
        PartRegistry.getInstance().registerPart(GateRepeater.class);
        PartRegistry.getInstance().registerPart(GateTransparentLatch.class);
        PartRegistry.getInstance().registerPart(GateSynchronizer.class);
        PartRegistry.getInstance().registerPart(Circuit3x3.class);
        PartRegistry.getInstance().registerPart(Circuit5x5.class);
        PartRegistry.getInstance().registerPart(Circuit7x7.class);

        // Lamps
        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            PartRegistry.getInstance().registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);

        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            PartRegistry.getInstance().registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);

        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            PartRegistry.getInstance().registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);

        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            PartRegistry.getInstance().registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);

        // Pneumatic Tubes
        PartRegistry.getInstance().registerPart(PneumaticTube.class);
        PartRegistry.getInstance().registerPart(PneumaticTubeOpaque.class);
        PartRegistry.getInstance().registerPart(RestrictionTube.class);
        PartRegistry.getInstance().registerPart(RestrictionTubeOpaque.class);
        PartRegistry.getInstance().registerPart(MagTube.class);
        PartRegistry.getInstance().registerPart(Accelerator.class);

        // Bluestone
        for (int bundled = 0; bundled < 2; bundled++) {
            PartRegistry.getInstance().registerPart(WireBluestone.class, bundled == 1);// Normal
            for (int i = 0; i < 16; i++)
                PartRegistry.getInstance().registerPart(WireBluestone.class, i, bundled == 1);// Colored
        }


        PartRegistry.getInstance().registerPart(WireBluePower.class);
    }
}
