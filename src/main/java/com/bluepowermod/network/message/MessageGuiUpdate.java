/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import com.bluepowermod.client.gui.IGuiButtonSensitive;
import com.bluepowermod.network.LocatedPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;

/**
 *
 * @author MineMaarten
 */

public class MessageGuiUpdate extends LocatedPacket<MessageGuiUpdate> {

    private int partId;
    private int icId; // only used with the Integrated Circuit
    private int messageId;
    private int value;

    public MessageGuiUpdate() {

    }
    public MessageGuiUpdate(Container container, int messageId, int value) {
        super();
        partId = -1;
        this.messageId = messageId;
        this.value = value;
    }

    @Override
    public void handleClientSide(PlayerEntity player) {

    }

    @Override
    public void handleServerSide(PlayerEntity player) {

            TileEntity te = player.world.getTileEntity(pos);
            if (te instanceof IGuiButtonSensitive) {
                ((IGuiButtonSensitive) te).onButtonPress(player, messageId, value);
            }
    }
}
