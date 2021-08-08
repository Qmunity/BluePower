/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier2;

import com.bluepowermod.container.ContainerRetriever;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.IFuzzyRetrieving;
import com.bluepowermod.tile.tier1.TileFilter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import javax.annotation.Nullable;

/**
 * @author MineMaarten
 */
public class TileRetriever extends TileFilter implements IFuzzyRetrieving {

    public int slotIndex;
    public int mode;


    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putByte("slotIndex", (byte) slotIndex);
        tag.putByte("mode", (byte) mode);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        slotIndex = tag.getByte("slotIndex");
        mode = tag.getByte("mode");
    }

    @Override
    public int getFuzzySetting() {
        return fuzzySetting;
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent(Refs.RETRIEVER_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
        return new ContainerRetriever(id, inventory, this);
    }

}
