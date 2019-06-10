/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import com.bluepowermod.network.LocatedPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import com.bluepowermod.helper.Debugger;

/**
 *
 * @author MineMaarten, amadornes
 */

public class MessageDebugBlock extends LocatedPacket<MessageDebugBlock> {

    public MessageDebugBlock() {

    }

    public MessageDebugBlock(BlockPos pos) {

        super(pos);
    }

    @Override
    public void handleClientSide(PlayerEntity player) {

        Debugger.indicateBlock(player.world, pos);
    }

    @Override
    public void handleServerSide(PlayerEntity player) {

    }

}
