package com.bluepowermod.api;

import net.minecraft.item.Item;

import com.bluepowermod.api.compat.IMultipartCompat;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BPApi {

    public static IMultipartCompat getMultipartCompat() {

        try {
            Class c = Class.forName("com.bluepowermod.compat.CompatibilityUtils");
            return (IMultipartCompat) c.getDeclaredMethod("getModule").invoke(c, Dependencies.FMP);
        } catch (Exception e) {
        }
        return null;
    }

    public static Item getItem(String name) {

        try {
            Class c = Class.forName("com.bluepowermod.init.BPItems");
            return (Item) c.getDeclaredField(name).get(c);
        } catch (Exception e) {
        }
        return null;
    }

    public static Item getBlock(String name) {

        try {
            Class c = Class.forName("com.bluepowermod.init.BPBlocks");
            return (Item) c.getDeclaredField(name).get(c);
        } catch (Exception e) {
        }
        return null;
    }
}
