package com.bluepowermod.init;

import net.minecraft.item.ItemDye;

import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.part.cable.bluestone.WireBluestone;
import com.bluepowermod.part.fluid.PartCastingTable;
import com.bluepowermod.part.fluid.PartFaucet;
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
import com.bluepowermod.part.tube.RestrictionTube;

public class PartRegister {

    public static void registerParts() {

        PartRegistry r = PartRegistry.getInstance();

        r.ICON_PART = "timer";
        // Gates
        r.registerPart(GateNot.class);
        r.registerPart(GateAnd.class);
        r.registerPart(GateTimer.class);
        r.registerPart(GateSequencer.class);
        r.registerPart(GateBuffer.class);
        r.registerPart(GateCounter.class);
        r.registerPart(GateMux.class);
        r.registerPart(GateNand.class);
        r.registerPart(GateOr.class);
        r.registerPart(GateNor.class);
        r.registerPart(GatePulseFormer.class);
        r.registerPart(GateRandomizer.class);
        r.registerPart(GateLightCell.class);
        r.registerPart(GateToggleLatch.class);
        r.registerPart(GateRSLatch.class);
        r.registerPart(GateXor.class);
        r.registerPart(GateXnor.class);
        r.registerPart(GateStateCell.class);
        r.registerPart(GateRepeater.class);
        r.registerPart(GateTransparentLatch.class);
        r.registerPart(Circuit3x3.class);
        r.registerPart(Circuit5x5.class);
        r.registerPart(Circuit7x7.class);

        // Lamps
        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            r.registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);

        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            r.registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);

        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            r.registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);

        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            r.registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);

        // Pneumatic Tubes
        r.registerPart(PneumaticTube.class);
        r.registerPart(RestrictionTube.class);
        r.registerPart(MagTube.class);
        r.registerPart(Accelerator.class);

        // Bluestone
        r.registerPart(WireBluestone.class);// Normal
        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            r.registerPart(WireBluestone.class, ItemDye.field_150922_c[i], ItemDye.field_150921_b[i].toLowerCase());// Colored

        // Casting table and faucet
        r.registerPart(PartCastingTable.class);
        r.registerPart(PartFaucet.class);
    }

}
