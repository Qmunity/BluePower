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

import com.bluepowermod.tile.BPBlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;

public class BlockCircuitDatabase extends BlockProjectTable {

    public BlockCircuitDatabase(Class<? extends TileBase> tileClass) {

        super(tileClass, BPBlockEntityType.CIRCUIT_TABLE);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (super.use(blockState, world, pos, player, hand, rayTraceResult) == InteractionResult.SUCCESS) {
            TileCircuitDatabase database = (TileCircuitDatabase) world.getBlockEntity(pos);
            database.clientCurrentTab = 0;
            if (!world.isClientSide) {
                //BPNetworkHandler.INSTANCE.sendTo(new MessageSendClientServerTemplates(new ItemStackDatabase().loadItemStacks()),
                        //(ServerPlayer) player);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

}
