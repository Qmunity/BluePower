/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.part.PartRegistry;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.part.gate.analogue.GateComparator;
import com.bluepowermod.part.gate.analogue.GateInverter;
import com.bluepowermod.part.gate.analogue.GateLightCell;
import com.bluepowermod.part.gate.digital.GateAnd;
import com.bluepowermod.part.gate.digital.GateBuffer;
import com.bluepowermod.part.gate.digital.GateCounter;
import com.bluepowermod.part.gate.digital.GateNand;
import com.bluepowermod.part.gate.digital.GateNor;
import com.bluepowermod.part.gate.digital.GateNot;
import com.bluepowermod.part.gate.digital.GateOr;
import com.bluepowermod.part.gate.digital.GatePulseFormer;
import com.bluepowermod.part.gate.digital.GateRandomizer;
import com.bluepowermod.part.gate.digital.GateRepeater;
import com.bluepowermod.part.gate.digital.GateSequencer;
import com.bluepowermod.part.gate.digital.GateStateCell;
import com.bluepowermod.part.gate.digital.GateSynchronizer;
import com.bluepowermod.part.gate.digital.GateTimer;
import com.bluepowermod.part.gate.digital.GateToggleLatch;
import com.bluepowermod.part.gate.ic.GateIntegratedCircuit;
import com.bluepowermod.part.gate.supported.GateNullCell;
import com.bluepowermod.part.gate.wireless.GateTransceiver;
import com.bluepowermod.part.lamp.PartCageLamp;
import com.bluepowermod.part.lamp.PartFixture;
import com.bluepowermod.part.tube.Accelerator;
import com.bluepowermod.part.tube.MagTube;
import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.part.tube.PneumaticTubeOpaque;
import com.bluepowermod.part.tube.RestrictionTube;
import com.bluepowermod.part.tube.RestrictionTubeOpaque;
import com.bluepowermod.part.wire.redstone.PartRedwireFace.PartRedwireFaceBundled;
import com.bluepowermod.part.wire.redstone.PartRedwireFace.PartRedwireFaceInsulated;
import com.bluepowermod.part.wire.redstone.PartRedwireFace.PartRedwireFaceUninsulated;
import com.bluepowermod.part.wire.redstone.PartRedwireFreestanding.PartRedwireFreestandingInsulated;
import com.bluepowermod.part.wire.redstone.PartRedwireFreestanding.PartRedwireFreestandingUninsulated;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PartManager {

    private static Map<String, PartInfo> parts = new LinkedHashMap<String, PartInfo>();

    public static void registerPart(Class<? extends BPPart> clazz, Object... arguments) {

        if (clazz == null)
            return;

        PartInfo info = new PartInfo(clazz, arguments);

        if (info.getType() == null)
            return;

        parts.put(info.getType(), info);
    }

    public static PartInfo getPartInfo(String type) {

        if (parts.containsKey(type))
            return parts.get(type);
        return null;
    }

    public static String getPartType(ItemStack item) {

        try {
            return ((ItemPart) item.getItem()).getPartType();
        } catch (Exception ex) {
        }
        return null;
    }

    public static BPPart createPart(ItemStack item) {

        return getPartInfo(getPartType(item)).create();
    }

    public static BPPart getExample(ItemStack item) {

        return getExample(getPartType(item));
    }

    public static BPPart getExample(String type) {

        return getPartInfo(type).getExample();
    }

    public static List<PartInfo> getRegisteredParts() {

        List<PartInfo> l = new ArrayList<PartInfo>();

        for (String s : parts.keySet())
            l.add(parts.get(s));

        return l;
    }

    public static void registerParts() {

        PartRegistry.registerFactory(new PartFactory());

        // Digital gates
        registerPart(GateAnd.class);
        registerPart(GateNot.class);
        registerPart(GateOr.class);
        registerPart(GateNand.class);
        registerPart(GateBuffer.class);
        // registerPart(GateXor.class);
        // registerPart(GateXnor.class);
        registerPart(GateNor.class);
        registerPart(GateTimer.class);
        registerPart(GateSequencer.class);
        registerPart(GateCounter.class);
        // registerPart(GateMux.class);
        registerPart(GatePulseFormer.class);
        registerPart(GateRandomizer.class);
        registerPart(GateToggleLatch.class);
        // registerPart(GateRSLatch.class);
        registerPart(GateStateCell.class);
        registerPart(GateRepeater.class);
        // registerPart(GateTransparentLatch.class);
        registerPart(GateSynchronizer.class);

        // Analog gates
        registerPart(GateInverter.class);
        registerPart(GateComparator.class);
        registerPart(GateLightCell.class);

        // Wireless gates
        registerPart(GateTransceiver.class, false, false);
        registerPart(GateTransceiver.class, true, false);
        registerPart(GateTransceiver.class, false, true);
        registerPart(GateTransceiver.class, true, true);

        // IC's
        registerPart(GateIntegratedCircuit.class, 3);
        registerPart(GateIntegratedCircuit.class, 5);
        registerPart(GateIntegratedCircuit.class, 7);

        // Supported gates
        registerPart(GateNullCell.class);

        // Lamps
        for (int i = 0; i < 2; i++)
            for (MinecraftColor c : MinecraftColor.VALID_COLORS)
                registerPart(PartCageLamp.class, c, i == 1);
        for (int i = 0; i < 2; i++)
            for (MinecraftColor c : MinecraftColor.VALID_COLORS)
                registerPart(PartFixture.class, c, i == 1);

        // Pneumatic Tubes
        registerPart(PneumaticTube.class);
        registerPart(PneumaticTubeOpaque.class);
        registerPart(RestrictionTube.class);
        registerPart(RestrictionTubeOpaque.class);
        registerPart(MagTube.class);
        registerPart(Accelerator.class);

        // Wires
        for (RedwireType type : RedwireType.values()) {
            registerPart(PartRedwireFaceUninsulated.class, type);
            for (MinecraftColor color : MinecraftColor.VALID_COLORS)
                registerPart(PartRedwireFaceInsulated.class, type, color);
            registerPart(PartRedwireFaceBundled.class, type, MinecraftColor.NONE);
            for (MinecraftColor color : MinecraftColor.VALID_COLORS)
                registerPart(PartRedwireFaceBundled.class, type, color);
        }
        for (RedwireType type : RedwireType.values()) {
            registerPart(PartRedwireFreestandingUninsulated.class, type);
            for (MinecraftColor color : MinecraftColor.VALID_COLORS)
                registerPart(PartRedwireFreestandingInsulated.class, type, color);
            // registerPart(PartRedwireFace.class, type, MinecraftColor.NONE, true);
            // for (MinecraftColor color : MinecraftColor.VALID_COLORS)
            // registerPart(PartRedwireFace.class, type, color, true);
        }
    }

    public static void registerItems() {

        for (String s : parts.keySet())
            parts.get(s).registerItem();
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenderers() {

        for (String s : parts.keySet())
            parts.get(s).registerRenderer();
    }
}
