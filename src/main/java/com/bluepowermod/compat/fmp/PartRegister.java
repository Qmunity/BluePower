package com.bluepowermod.compat.fmp;

import com.bluepowermod.part.cable.bluepower.WireBluePower;
import com.bluepowermod.part.gate.*;
import net.minecraft.item.ItemDye;

import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.part.cable.bluestone.WireBluestone;
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
        
        // Test cable
        PartRegistry.getInstance().registerPart(WireBluestone.class);

        PartRegistry.getInstance().registerPart(WireBluePower.class);
        /*
         * // Red alloy registerPart(CableWall.class); // Uncovered for (int i = 0; i < ItemDye.field_150922_c.length; i++)
         * registerPart(CableWall.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i]); // Covered
         */
    }
    
}
