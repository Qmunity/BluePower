/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.part.gate.GateAnd;
import com.bluepowermod.part.gate.GateNot;
import com.qmunity.lib.part.PartRegistry;

public class PartManager {

    private static Map<String, PartInfo> parts = new HashMap<String, PartInfo>();

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
            NBTTagCompound tag = item.getTagCompound();
            return tag.getString("id");
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

        // Gates
        PartRegistry.registerFactory(new PartFactory());
        registerPart(GateAnd.class);
        registerPart(GateNot.class);
        // registerPart(GateTimer.class);
        // registerPart(GateSequencer.class);
        // registerPart(GateBuffer.class);
        // registerPart(GateCounter.class);
        // registerPart(GateMux.class);
        // registerPart(GateNand.class);
        // registerPart(GateOr.class);
        // registerPart(GateNor.class);
        // registerPart(GatePulseFormer.class);
        // registerPart(GateRandomizer.class);
        // registerPart(GateLightCell.class);
        // registerPart(GateToggleLatch.class);
        // registerPart(GateRSLatch.class);
        // registerPart(GateXor.class);
        // registerPart(GateXnor.class);
        // registerPart(GateStateCell.class);
        // registerPart(GateRepeater.class);
        // registerPart(GateTransparentLatch.class);
        // registerPart(GateSynchronizer.class);
        // registerPart(Circuit3x3.class);
        // registerPart(Circuit5x5.class);
        // registerPart(Circuit7x7.class);
        //
        // Lamps
        // for (int i = 0; i < ItemDye.field_150922_c.length; i++)
        // registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);
        //
        // for (int i = 0; i < ItemDye.field_150922_c.length; i++)
        // registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);
        //
        // for (int i = 0; i < ItemDye.field_150922_c.length; i++)
        // registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);
        //
        // for (int i = 0; i < ItemDye.field_150922_c.length; i++)
        // registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);
        //
        // // Pneumatic Tubes
        // registerPart(PneumaticTube.class);
        // registerPart(PneumaticTubeOpaque.class);
        // registerPart(RestrictionTube.class);
        // registerPart(RestrictionTubeOpaque.class);
        // registerPart(MagTube.class);
        // registerPart(Accelerator.class);

        // Bluestone
        // for (int bundled = 0; bundled < 2; bundled++) {
        // registerPart(WireBluestone.class, bundled == 1);// Normal
        // for (int i = 0; i < 16; i++)
        // registerPart(WireBluestone.class, i, bundled == 1);// Colored
        // }
    }
}
