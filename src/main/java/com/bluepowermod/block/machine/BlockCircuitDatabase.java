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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bluepowermod.helper.ItemStackDatabase;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageSendClientServerTemplates;
import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;

public class BlockCircuitDatabase extends BlockProjectTable {

    public BlockCircuitDatabase(Class<? extends TileBase> tileClass) {

        super(tileClass);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ)) {
            TileCircuitDatabase database = (TileCircuitDatabase) world.getTileEntity(pos);
            database.clientCurrentTab = 0;
            if (!world.isRemote) {
                BPNetworkHandler.INSTANCE.sendTo(new MessageSendClientServerTemplates(new ItemStackDatabase().loadItemStacks()),
                        (EntityPlayerMP) player);
            }
            return true;
        } else {
            return false;
        }
    }

}
