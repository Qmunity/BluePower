package net.quetzi.bluepower.api.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.part.gate.GateBase;
import net.quetzi.bluepower.references.Refs;

public class PartRegistry {
    
    private static Map<String, Class<? extends BPPart>> parts = new HashMap<String, Class<? extends BPPart>>();
    
    private PartRegistry() {
    
    }
    
    /**
     * Register a part
     * 
     * @param id
     *            Part identifier (same as the one returned by the part)
     * @param part
     *            Part class
     */
    public static void registerPart(String id, Class<? extends BPPart> part) {
    
        if (id == null || id.trim().isEmpty()) return;
        if (part == null) return;
        if (parts.containsKey(part)) return;
        
        parts.put(id, part);
    }
    
    /**
     * Creates a part from its ID
     * 
     * @param id
     *            The part ID
     * @param isMultipart
     *            Whether it's using a multipart id or not (this is usually
     *            false)
     * @return A new instance of the part or null if it couldn't be found
     */
    public static BPPart createPart(String id, boolean isMultipart) {
    
        try {
            return getPartClass(id, isMultipart).newInstance();
        } catch (Exception ex) {
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
    
    public static Map<String, Class<? extends BPPart>> getMappings() {
    
        return Collections.unmodifiableMap(parts);
    }
    
    public static List<String> getRegisteredParts() {
    
        return Collections.unmodifiableList(new ArrayList<String>(parts
                .keySet()));
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
    
    public static Class<? extends BPPart> getPartClass(String id) {
    
        return getPartClass(id, false);
    }
    
    public static Class<? extends BPPart> getPartClass(String id,
            boolean isMultipart) {
    
        try {
            for (String s : parts.keySet()) {
                if (((isMultipart ? Refs.MODID + "_" + s : s)).equals(id)) {
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
    
    public static Class<? extends BPPart> getPartClassFromItem(ItemStack is) {
    
        String id = getPartIdFromItem(is);
        return getPartClass(id);
    }
    
    /**
     * Gets the part ID stored in the item
     * @param is Item to get the part ID from
     * @return The part ID
     */
    public static String getPartIdFromItem(ItemStack is) {
    
        NBTTagCompound tag = is.getTagCompound();
        return tag.getString("id");
    }
    
    public static void init() {
    
        registerPart("gatebase", GateBase.class);
    }
    
}
