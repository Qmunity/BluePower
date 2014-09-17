/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part;

import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.ComparatorCreativeTabIndex;
import com.bluepowermod.api.part.IPartRegistry;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.registry.GameRegistry;

public class PartRegistry implements IPartRegistry {

    private static PartRegistry INSTANCE = new PartRegistry();
    private final Map<String, Entry<Class<? extends BPPart>, Object[]>> parts = new LinkedHashMap<String, Entry<Class<? extends BPPart>, Object[]>>();
    private final Map<String, BPPart> samples = new LinkedHashMap<String, BPPart>();
    public static Map<String, Item> multipartItems = new HashMap<String, Item>();

    public String ICON_PART;

    private PartRegistry() {

    }

    public static PartRegistry getInstance() {

        return INSTANCE;
    }

    /**
     * Register a part
     * 
     * @param part
     *            Part class
     */
    @Override
    public void registerPart(Class<? extends BPPart> part, Object... constructorArgs) {

        if (part == null)
            return;
        Entry<Class<? extends BPPart>, Object[]> e = new AbstractMap.SimpleEntry<Class<? extends BPPart>, Object[]>(part, constructorArgs);
        if (parts.containsKey(e))
            return;

        parts.put("tmp", e);
        BPPart p = createPart("tmp");
        samples.put(p.getType(), p);
        parts.remove("tmp");
        parts.put(p.getType(), e);

        Item item = BPApi.getInstance().getMultipartCompat().getNewMultipartItem(p.getType());
        multipartItems.put(p.getType(), item);
        GameRegistry.registerItem(item, "part." + p.getType());
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
    @Override
    public BPPart createPart(String id, boolean isMultipart) {

        try {
            Entry<Class<? extends BPPart>, Object[]> e = getPartData(id, isMultipart);
            if (e == null)
                return null;
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
    @Override
    public BPPart createPart(String id) {

        return createPart(id, false);
    }

    @Override
    public Map<String, Entry<Class<? extends BPPart>, Object[]>> getMappings() {

        return Collections.unmodifiableMap(parts);
    }

    @Override
    public List<String> getRegisteredParts() {

        return Collections.unmodifiableList(new ArrayList<String>(parts.keySet()));
    }

    @Override
    public List<String> getRegisteredPartsForTab(CreativeTabs tab) {

        List<String> partIds = new ArrayList<String>();
        List<BPPart> parts = new ArrayList<BPPart>();

        if (tab != null) {
            for (BPPart p : samples.values())
                if (Arrays.asList(p.getCreativeTabs()).contains(tab))
                    parts.add(p);
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
    @Override
    public ItemStack getItemForPart(String id) {

        if (multipartItems.containsKey(id)) {
            ItemStack is = new ItemStack(multipartItems.get(id));

            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("id", id);

            is.setTagCompound(tag);
            return is;
        }
        return null;
    }

    @Override
    public ItemStack getItemForPart(String id, int stackSize) {

        ItemStack is = getItemForPart(id);
        if (is != null) {
            is.stackSize = stackSize;
            return is;
        } else {
            return null;
        }
    }

    /**
     * Gets the part id stored in the item and creates a new part with that id
     * 
     * @param is
     *            The item to get the part id from
     * @return A new instance of the part or null if it couldn't be found
     */
    @Override
    public BPPart createPartFromItem(ItemStack is) {

        String id = getPartIdFromItem(is);
        return createPart(id);
    }

    @Override
    public Entry<Class<? extends BPPart>, Object[]> getPartData(String id) {

        return getPartData(id, false);
    }

    @Override
    public Entry<Class<? extends BPPart>, Object[]> getPartData(String id, boolean isMultipart) {

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

    @Override
    public Entry<Class<? extends BPPart>, Object[]> getPartDataFromItem(ItemStack is) {

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
    @Override
    public String getPartIdFromItem(ItemStack is) {

        try {
            NBTTagCompound tag = is.getTagCompound();
            return tag.getString("id");
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    public boolean hasCustomItemEntity(ItemStack is) {

        String id = getPartIdFromItem(is);
        BPPart part = samples.get(id);
        return part != null && part.hasCustomItemEntity();
    }

    @Override
    public EntityItem createItemEntityForPart(String id, World w, double x, double y, double z, ItemStack item) {

        BPPart part = samples.get(id);
        if (part == null)
            return null;
        return part.createItemEntity(w, x, y, z, item);
    }

    @Override
    public EntityItem createItemEntityForStack(World w, double x, double y, double z, ItemStack item) {

        String id = getPartIdFromItem(item);
        BPPart part = samples.get(id);
        if (part == null)
            return null;
        return part.createItemEntity(w, x, y, z, item);
    }

}
