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
 */

package com.bluepowermod.block.machine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;

public class BlockCircuitDatabase extends BlockProjectTable {

    public BlockCircuitDatabase(Class<? extends TileBase> tileClass) {

        super(tileClass);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (super.onBlockActivated(blockState, world, pos, player, hand, rayTraceResult) == ActionResultType.SUCCESS) {
            TileCircuitDatabase database = (TileCircuitDatabase) world.getTileEntity(pos);
            database.clientCurrentTab = 0;
            if (!world.isRemote) {
                //BPNetworkHandler.INSTANCE.sendTo(new MessageSendClientServerTemplates(new ItemStackDatabase().loadItemStacks()),
                        //(ServerPlayerEntity) player);
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

}
