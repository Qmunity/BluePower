/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.compat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bluepowermod.compat.cc.CompatModuleCC;
import com.bluepowermod.compat.fmp.CompatModuleFMP;
import com.bluepowermod.compat.hydcraft.CompatModuleHydCraft;
import com.bluepowermod.compat.ic2.CompatModuleIC2;
import com.bluepowermod.compat.waila.CompatModuleWaila;
import com.bluepowermod.util.Dependencies;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompatibilityUtils {

    private static Map<String, CompatModule> modules = new HashMap<String, CompatModule>();

    private CompatibilityUtils() {

    }

    public static void registerModule(String modid, Class<? extends CompatModule> module, Class<? extends CompatModule> alternate) {

        registerModule(modid, module.getName(), alternate == null ? null : alternate.getName());
    }

    public static void registerModule(String modid, String module, String alternate) {

        if (modid == null || modid.trim().isEmpty())
            return;
        if (module == null)
            return;
        if (modules.containsKey(module))
            return;

        if (Loader.isModLoaded(modid)) {
            try {
                Class<?> c = Class.forName(module);
                if (!CompatModule.class.isAssignableFrom(c))
                    return;
                modules.put(modid, (CompatModule) c.newInstance());
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (alternate == null || alternate.trim().isEmpty())
            return;

        try {
            Class<?> c2 = Class.forName(alternate);
            if (!CompatModule.class.isAssignableFrom(c2))
                return;
            modules.put(modid, (CompatModule) c2.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<CompatModule> getLoadedModules() {

        return Collections.unmodifiableList(new ArrayList<CompatModule>(modules.values()));
    }

    public static CompatModule getModule(String modid) {

        try {
            return modules.get(modid);
        } catch (Exception ex) {
        }

        return null;
    }

    public static void preInit(FMLPreInitializationEvent ev) {

        for (CompatModule m : getLoadedModules())
            m.preInit(ev);
    }

    public static void init(FMLInitializationEvent ev) {

        for (CompatModule m : getLoadedModules())
            m.init(ev);
    }

    public static void postInit(FMLPostInitializationEvent ev) {

        for (CompatModule m : getLoadedModules())
            m.postInit(ev);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {

        for (CompatModule m : getLoadedModules())
            m.registerRenders();
    }

    /**
     * Register your modules here
     */
    static {
        registerModule(Dependencies.FMP, CompatModuleFMP.class, null);
        registerModule(Dependencies.COMPUTER_CRAFT, CompatModuleCC.class, null);
        registerModule(Dependencies.WAILA, CompatModuleWaila.class, null);
        registerModule(Dependencies.IC2, CompatModuleIC2.class, null);
        registerModule(Dependencies.HC, CompatModuleHydCraft.class, null);
    }

}
