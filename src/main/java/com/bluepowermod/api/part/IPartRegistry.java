/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.part;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPartRegistry {

    void registerPart(Class<? extends BPPart> part, Object... constructorArgs);

    /**
     * Creates a part from its ID
     * 
     * @param id
     *            The part ID
     * @param isMultipart
     *            Whether it's using a multipart id or not (this is usually false)
     * @return A new instance of the part or null if it couldn't be found
     */
    public BPPart createPart(String id, boolean isMultipart);

    /**
     * Creates a part from its ID
     * 
     * @param id
     *            The part ID
     * @return A new instance of the part or null if it couldn't be found
     */
    public BPPart createPart(String id);

    public Map<String, Entry<Class<? extends BPPart>, Object[]>> getMappings();

    public List<String> getRegisteredParts();

    public List<String> getRegisteredPartsForTab(CreativeTabs tab);

    /**
     * Gets the part's item from an ID
     * 
     * @param id
     *            The part's ID
     * @return An item with the part ID
     */
    public ItemStack getItemForPart(String id);

    public ItemStack getItemForPart(String id, int stackSize);

    /**
     * Gets the part id stored in the item and creates a new part with that id
     * 
     * @param is
     *            The item to get the part id from
     * @return A new instance of the part or null if it couldn't be found
     */
    public BPPart createPartFromItem(ItemStack is);

    public Entry<Class<? extends BPPart>, Object[]> getPartData(String id);

    public Entry<Class<? extends BPPart>, Object[]> getPartData(String id, boolean isMultipart);

    public Entry<Class<? extends BPPart>, Object[]> getPartDataFromItem(ItemStack is);

    /**
     * Gets the part ID stored in the item
     * 
     * @param is
     *            Item to get the part ID from
     * @return The part ID
     */
    public String getPartIdFromItem(ItemStack is);

    public boolean hasCustomItemEntity(ItemStack is);

    public EntityItem createItemEntityForPart(String id, World w, double x, double y, double z, ItemStack item);

    public EntityItem createItemEntityForStack(World w, double x, double y, double z, ItemStack item);
}
