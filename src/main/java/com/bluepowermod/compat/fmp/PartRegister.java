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
import com.bluepowermod.part.tube.RestrictionTube;

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
        PartRegistry.getInstance().registerPart(RestrictionTube.class);
        PartRegistry.getInstance().registerPart(MagTube.class);
        PartRegistry.getInstance().registerPart(Accelerator.class);

        // Bluestone
        PartRegistry.getInstance().registerPart(WireBluestone.class);// Normal
        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            PartRegistry.getInstance().registerPart(WireBluestone.class, ItemDye.field_150922_c[i], ItemDye.field_150921_b[i].toLowerCase());// Colored

        PartRegistry.getInstance().registerPart(WireBluePower.class);
    }

}
