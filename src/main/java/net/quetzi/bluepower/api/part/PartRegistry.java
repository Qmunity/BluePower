/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.api.part;

import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.part.gate.GateAnd;
import net.quetzi.bluepower.part.gate.GateBuffer;
import net.quetzi.bluepower.part.gate.GateCounter;
import net.quetzi.bluepower.part.gate.GateMux;
import net.quetzi.bluepower.part.gate.GateNand;
import net.quetzi.bluepower.part.gate.GateNor;
import net.quetzi.bluepower.part.gate.GateNot;
import net.quetzi.bluepower.part.gate.GateOr;
import net.quetzi.bluepower.part.gate.GateSequencer;
import net.quetzi.bluepower.part.gate.GateTimer;
import net.quetzi.bluepower.part.lamp.PartCageLamp;
import net.quetzi.bluepower.part.tube.PneumaticTube;
import net.quetzi.bluepower.references.Refs;

public class PartRegistry {
    
    private static Map<String, Entry<Class<? extends BPPart>, Object[]>> parts   = new HashMap<String, Entry<Class<? extends BPPart>, Object[]>>();
    private static Map<String, BPPart>                                   samples = new HashMap<String, BPPart>();
    
    public static String                                                 ICON_PART;
    
    private PartRegistry() {
    
    }
    
    /**
     * Register a part
     * 
     * @param part
     *            Part class
     */
    public static void registerPart(Class<? extends BPPart> part, Object... constructorArgs) {
    
        if (part == null) return;
        Entry<Class<? extends BPPart>, Object[]> e = new AbstractMap.SimpleEntry<Class<? extends BPPart>, Object[]>(part, constructorArgs);
        if (parts.containsKey(e)) return;
        
        parts.put("tmp", e);
        BPPart p = createPart("tmp");
        samples.put(p.getType(), p);
        parts.remove("tmp");
        parts.put(p.getType(), e);
    }
    
    /**
     * Creates a part from its ID
     * 
     * @param id
     *            The part ID
     * @param isMultipart
     *            Whether it's using a multipart id or not (this is usually false)
     * @return A new instance of the part or null if it couldn't be found
     */
    public static BPPart createPart(String id, boolean isMultipart) {
    
        try {
            Entry<Class<? extends BPPart>, Object[]> e = getPartData(id, isMultipart);
            if (e == null) return null;
            Class<? extends BPPart> c = e.getKey();
            Object[] args = e.getValue();
            Class<?>[] argsClasses = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++)
                argsClasses[i] = args[i].getClass();
            
            Constructor<? extends BPPart> cons = c.getConstructor(argsClasses);
            boolean wasAccessible = cons.isAccessible();
            cons.setAccessible(true);
            BPPart inst = cons.newInstance(args);
            cons.setAccessible(wasAccessible);
            
            return inst;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Creates a part from its ID
     * 
     * @param id
     *            The part ID
     * @return A new instance of the part or null if it couldn't be found
     */
    public static BPPart createPart(String id) {
    
        return createPart(id, false);
    }
    
    public static Map<String, Entry<Class<? extends BPPart>, Object[]>> getMappings() {
    
        return Collections.unmodifiableMap(parts);
    }
    
    public static List<String> getRegisteredParts() {
    
        return Collections.unmodifiableList(new ArrayList<String>(parts.keySet()));
    }
    
    public static List<String> getRegisteredPartsForTab(CreativeTabs tab) {
    
        List<String> parts = new ArrayList<String>();
        
        if (tab != null) {
            for (String s : PartRegistry.samples.keySet())
                if (Arrays.asList(samples.get(s).getCreativeTabs()).contains(tab)) parts.add(s);
        }
        
        return Collections.unmodifiableList(new ArrayList<String>(parts));
    }
    
    /**
     * Gets the part's item from an ID
     * 
     * @param id
     *            The part's ID
     * @return An item with the part ID
     */
    public static ItemStack getItemForPart(String id) {
    
        if (parts.containsKey(id)) {
            ItemStack is = new ItemStack(BPItems.multipart);
            
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("id", id);
            
            is.setTagCompound(tag);
            
            return is;
        }
        
        return null;
    }
    
    /**
     * Gets the part id stored in the item and creates a new part with that id
     * 
     * @param is
     *            The item to get the part id from
     * @return A new instance of the part or null if it couldn't be found
     */
    public static BPPart createPartFromItem(ItemStack is) {
    
        String id = getPartIdFromItem(is);
        return createPart(id);
    }
    
    public static Entry<Class<? extends BPPart>, Object[]> getPartData(String id) {
    
        return getPartData(id, false);
    }
    
    public static Entry<Class<? extends BPPart>, Object[]> getPartData(String id, boolean isMultipart) {
    
        try {
            for (String s : parts.keySet()) {
                if ((isMultipart ? Refs.MODID + "_" + s : s).equals(id)) {
                    try {
                        return parts.get(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static Entry<Class<? extends BPPart>, Object[]> getPartDataFromItem(ItemStack is) {
    
        String id = getPartIdFromItem(is);
        return getPartData(id);
    }
    
    /**
     * Gets the part ID stored in the item
     * 
     * @param is
     *            Item to get the part ID from
     * @return The part ID
     */
    public static String getPartIdFromItem(ItemStack is) {
    
        try {
            NBTTagCompound tag = is.getTagCompound();
            return tag.getString("id");
        } catch (Exception ex) {
        }
        return null;
    }
    
    /**
     * Gets the metadata for the multipart itemstack passed as an argument
     * @param is
     *            ItemsStack to get the metadata from
     * @return The metadata that stack should have
     */
    public static int getStackMetadata(ItemStack is) {
    
        String id = getPartIdFromItem(is);
        if (id == null) return 0;
        int i = 0;
        for (String s : parts.keySet()) {
            if (s.equals(id)) break;
            i++;
        }
        return i;
    }
    
    public static void init() {
    
        ICON_PART = "timer";
        // Gates
        registerPart(GateNot.class);
        registerPart(GateAnd.class);
        registerPart(GateTimer.class);
        registerPart(GateSequencer.class);
        registerPart(GateBuffer.class);
        registerPart(GateCounter.class);
        registerPart(GateMux.class);
        registerPart(GateNand.class);
        registerPart(GateOr.class);
        registerPart(GateNor.class);
        
        // Lamps
        registerPart(PartCageLamp.class, "white", 0xDDDDDD);
        registerPart(PartCageLamp.class, "orange", 0xDB7D3E);
        registerPart(PartCageLamp.class, "magenta", 0xB350BC);
        registerPart(PartCageLamp.class, "lightblue", 0x6B8AC9);
        registerPart(PartCageLamp.class, "yellow", 0xB1A627);
        registerPart(PartCageLamp.class, "lime", 0x41AE38);
        registerPart(PartCageLamp.class, "pink", 0xD08499);
        registerPart(PartCageLamp.class, "gray", 0x404040);
        registerPart(PartCageLamp.class, "lightgray", 0x9AA1A1);
        registerPart(PartCageLamp.class, "cyan", 0x2E6E89);
        registerPart(PartCageLamp.class, "purple", 0x7E3DB5);
        registerPart(PartCageLamp.class, "blue", 0x2E388D);
        registerPart(PartCageLamp.class, "brown", 0x4F321F);
        registerPart(PartCageLamp.class, "green", 0x35461B);
        registerPart(PartCageLamp.class, "red", 0x963430);
        registerPart(PartCageLamp.class, "black", 0x191616);
        
        // Pneumatic Tubes
        registerPart(PneumaticTube.class);
    }
    
}
