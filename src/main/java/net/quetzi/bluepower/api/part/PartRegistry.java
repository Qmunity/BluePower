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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.part.cable.RedAlloyWire;
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
import net.quetzi.bluepower.part.lamp.PartFixture;
import net.quetzi.bluepower.part.lamp.PartLamp;
import net.quetzi.bluepower.part.tube.PneumaticTube;
import net.quetzi.bluepower.references.Refs;

public class PartRegistry {
    
    private static Map<String, Entry<Class<? extends BPPart>, Object[]>> parts   = new LinkedHashMap<String, Entry<Class<? extends BPPart>, Object[]>>();
    private static Map<String, BPPart>                                   samples = new LinkedHashMap<String, BPPart>();
    
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
    
        List<String> partIds = new ArrayList<String>();
        List<BPPart> parts = new ArrayList<BPPart>();
        
        if (tab != null) {
            for (BPPart p : PartRegistry.samples.values())
                if (Arrays.asList(p.getCreativeTabs()).contains(tab)) parts.add(p);
            Collections.sort(parts, new ComparatorCreativeTabIndex(tab));
            for (BPPart p : parts)
                partIds.add(p.getType());
        }
        
        return Collections.unmodifiableList(new ArrayList<String>(partIds));
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
        for (int i = 0; i < ItemDye.field_150922_c.length; i++){
            registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);
            registerPart(PartLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);
            registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);
            
            registerPart(PartCageLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);
            registerPart(PartLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);
            registerPart(PartFixture.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], true);
        }
        
        // Pneumatic Tubes
        registerPart(PneumaticTube.class);
        
        // Red alloy
        registerPart(RedAlloyWire.class); // Uncovered
        for (int i = 0; i < ItemDye.field_150922_c.length; i++)
            registerPart(RedAlloyWire.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i]); // Covered
    }
    
}
