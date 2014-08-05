package com.bluepowermod.compat.fmp;

import net.minecraft.item.ItemDye;

import com.bluepowermod.api.part.PartRegistry;
import com.bluepowermod.part.cable.CableWallImpl;
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
import com.bluepowermod.part.gate.GateSequencer;
import com.bluepowermod.part.gate.GateTimer;
import com.bluepowermod.part.gate.GateToggleLatch;
import com.bluepowermod.part.gate.GateXnor;
import com.bluepowermod.part.gate.GateXor;
import com.bluepowermod.part.lamp.PartCageLamp;
import com.bluepowermod.part.lamp.PartFixture;
import com.bluepowermod.part.tube.Accelerator;
import com.bluepowermod.part.tube.MagTube;
import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.part.tube.RestrictionTube;

public class PartRegister {

    public static void registerParts() {

        PartRegistry.ICON_PART = "timer";
        // Gates
        PartRegistry.registerPart(GateNot.class);
        PartRegistry.registerPart(GateAnd.class);
        PartRegistry.registerPart(GateTimer.class);
        PartRegistry.registerPart(GateSequencer.class);
        PartRegistry.registerPart(GateBuffer.class);
        PartRegistry.registerPart(GateCounter.class);
        PartRegistry.registerPart(GateMux.class);
        PartRegistry.registerPart(GateNand.class);
        PartRegistry.registerPart(GateOr.class);
        PartRegistry.registerPart(GateNor.class);
        PartRegistry.registerPart(GatePulseFormer.class);
        PartRegistry.registerPart(GateRandomizer.class);
        PartRegistry.registerPart(GateLightCell.class);
        PartRegistry.registerPart(GateToggleLatch.class);
        PartRegistry.registerPart(GateRSLatch.class);
        PartRegistry.registerPart(GateXor.class);
        PartRegistry.registerPart(GateXnor.class);

        // Lamps
        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            PartRegistry.registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);

        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            PartRegistry.registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);

        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            PartRegistry.registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);
        /*
         * for (int i = 0; i < ItemDye.field_150922_c.length; i++) registerPart(PartLamp.class, ItemDye.field_150921_b[i].toLowerCase(),
         * ItemDye.field_150922_c[i], true);
         */
        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            PartRegistry.registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);

        // Pneumatic Tubes
        PartRegistry.registerPart(PneumaticTube.class);
        PartRegistry.registerPart(RestrictionTube.class);
        PartRegistry.registerPart(MagTube.class);
        PartRegistry.registerPart(Accelerator.class);

        // Test cable
        PartRegistry.registerPart(CableWallImpl.class);
        /*
         * // Red alloy registerPart(CableWall.class); // Uncovered for (int i = 0; i < ItemDye.field_150922_c.length; i++)
         * registerPart(CableWall.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i]); // Covered
         */
    }

}
