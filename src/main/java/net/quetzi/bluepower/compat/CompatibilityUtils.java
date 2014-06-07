package net.quetzi.bluepower.compat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.quetzi.bluepower.references.Dependencies;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatibilityUtils {
    
    private CompatibilityUtils() {
    
    }
    
    private static Map<String, CompatModule> modules = new HashMap<String, CompatModule>();
    
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
    
        return Collections.unmodifiableList(new ArrayList<CompatModule>(modules
                .values()));
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
        registerModule(Dependencies.FMP,
                "net.quetzi.bluepower.compat.fmp.CompatModuleFMP");
    }
    
}
