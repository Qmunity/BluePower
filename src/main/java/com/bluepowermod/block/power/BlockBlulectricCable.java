/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.power;

import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileBlulectricCable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricCable extends BlockBPCableBase {


    public BlockBlulectricCable() {
        super(2, 2);
        setRegistryName(Refs.MODID + ":" + Refs.BLULECTRICCABLE_NAME);
    }

    @Override
    public boolean hasBlockEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockState state, BlockGetter world) {
        return new TileBlulectricCable();
    }

    @Override
    protected Capability<?> getCapability() {
        return CapabilityBlutricity.BLUTRICITY_CAPABILITY;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops =  super.getDrops(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }

}
