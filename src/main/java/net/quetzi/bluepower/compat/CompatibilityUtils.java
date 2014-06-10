/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.compat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.quetzi.bluepower.references.Dependencies;

import java.util.*;

public class CompatibilityUtils {
    
    private static Map<String, CompatModule> modules = new HashMap<String, CompatModule>();
    
    private CompatibilityUtils() {
    
    }
    
    public static void registerModule(String modid, Class<? extends CompatModule> module) {
    
        registerModule(modid, module.getName());
    }
    
    public static void registerModule(String modid, String module) {
    
        if (modid == null || modid.trim().isEmpty()) return;
        if (!Loader.isModLoaded(modid)) return;
        if (module == null) return;
        if (modules.containsKey(module)) return;
        
        Class<?> c;
        try {
            c = Class.forName(module);
            if (!CompatModule.class.isAssignableFrom(c)) return;
            modules.put(modid, (CompatModule) c.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<CompatModule> getLoadedModules() {
    
        return Collections.unmodifiableList(new ArrayList<CompatModule>(modules.values()));
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
    
    /**
     * Register your modules here
     */
    static {
        registerModule(Dependencies.FMP, "net.quetzi.bluepower.compat.fmp.CompatModuleFMP");
    }
    
}
