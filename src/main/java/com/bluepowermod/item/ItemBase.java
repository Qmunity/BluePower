/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 *
 */

/*
 * @author Quetzi
 */

package com.bluepowermod.item;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.init.BPItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import com.bluepowermod.reference.Refs;
import net.minecraftforge.event.RegistryEvent;

public class ItemBase extends Item {

    public ItemBase(Properties properties) {
        super(properties.group(BPCreativeTabs.items));
        BPItems.itemList.add(this);
    }

    public ItemBase(Properties properties, ItemGroup group) {
        super(properties.group(group));
        BPItems.itemList.add(this);
    }

}
