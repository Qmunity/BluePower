/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.item;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileInsulatedWire;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;

public class ItemPaintBrush extends ItemDamageableColorableOverlay {
    private final MinecraftColor color;

    public ItemPaintBrush() {
        super(Refs.PAINTBRUSH_NAME + "_blank", new Properties());
        color = MinecraftColor.ANY;
    }

    public ItemPaintBrush(MinecraftColor color) {
        super(color, Refs.PAINTBRUSH_NAME, new Properties());
        this.color = color;
    }

    @Override
    protected int getMaxUses() {
        return 256;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        TileEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());
        boolean changed = false;
        if(tile instanceof TileInsulatedWire){
           changed = ((TileInsulatedWire) tile).setColor(color);
        if(changed && context.getPlayer() != null)
            context.getPlayer().setItemInHand(context.getHand(), new ItemStack(BPItems.paint_brush.get(0)));
        }
        return super.useOn(context);
    }
}
