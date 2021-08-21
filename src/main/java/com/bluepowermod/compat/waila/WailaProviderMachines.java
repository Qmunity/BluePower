/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.waila;

import com.bluepowermod.tile.TileMachineBase;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author amadornes
 *
 */
public class WailaProviderMachines implements IServerDataProvider<BlockEntity> {

    private List<String> info = new ArrayList<>();

    @Override
    public void appendServerData(CompoundTag CompoundTag, ServerPlayer serverPlayerEntity, Level world, BlockEntity blockEntity, boolean b) {
        if(blockEntity instanceof TileMachineBase && world.isClientSide) {
            TileMachineBase machine = (TileMachineBase) blockEntity;

            machine.addWailaInfo(info);
            //TODO: Check this works and add the engine
            CompoundTag.getAllKeys().addAll(info);
            info.clear();
        }
    }
}
