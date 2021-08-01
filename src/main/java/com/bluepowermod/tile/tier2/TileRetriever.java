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
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.StringTextComponent;

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
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
        slotIndex = tag.getByte("slotIndex");
        mode = tag.getByte("mode");
    }

    @Override
    public int getFuzzySetting() {
        return fuzzySetting;
    }

    @Override
    public Component getDisplayName() {
        return new StringTextComponent(Refs.RETRIEVER_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, PlayerInventory inventory, Player playerEntity) {
        return new ContainerRetriever(id, inventory, this);
    }

}
