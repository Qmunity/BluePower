/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.item;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.client.render.IBPColoredItem;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;

/**
 * @author MineMaarten
 */
public class ItemColorableOverlay extends ItemBase implements IBPColoredItem {

    private final MinecraftColor color;

    public ItemColorableOverlay(Properties properties) {
        super(properties);
        this.color = MinecraftColor.NONE;
    }
    public ItemColorableOverlay(MinecraftColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public int getColor(ItemStack itemStack, int renderPass) {
        return renderPass == 0 ? -1 : FastColor.ARGB32.opaque(color.getHex());
    }
}
