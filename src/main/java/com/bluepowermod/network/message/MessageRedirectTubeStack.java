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

package com.bluepowermod.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.part.tube.TubeLogic;
import com.bluepowermod.part.tube.TubeStack;

public class MessageRedirectTubeStack extends LocationIntPacket<MessageRedirectTubeStack> {

    private TubeStack stack;

    public MessageRedirectTubeStack() {

    }

    public MessageRedirectTubeStack(PneumaticTube tube, TubeStack stack) {

        super(tube.getX(), tube.getY(), tube.getZ());
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);
        stack.writeToPacket(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);
        stack = TubeStack.loadFromPacket(buf);
    }

    @Override
    public void handleClientSide(MessageRedirectTubeStack message, EntityPlayer player) {

        PneumaticTube tube = MultipartCompatibility.getPart(player.worldObj, message.x, message.y, message.z, PneumaticTube.class);
        if (tube == null)
            return;
        TubeLogic logic = tube.getLogic();
        if (logic == null)
            return;
        logic.onClientTubeRedirectPacket(message.stack);

    }

    @Override
    public void handleServerSide(MessageRedirectTubeStack message, EntityPlayer player) {

    }

}
