/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import io.netty.buffer.ByteBuf;

import java.util.List;

import mcmultipart.api.container.IMultipartContainer;
import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import com.bluepowermod.BluePower;
import com.bluepowermod.part.IGuiButtonSensitive;

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

    /**
     *
     * @param part
     *            should also implement IGuiButtonSensitive to be able to receive this packet.
     * @param messageId
     * @param value
     */
    public MessageGuiUpdate(IMultipartTile part, int messageId, int value) {

        super(part.getPos());

        // if (part instanceof GateBase && ((GateBase) part).parentCircuit != null) {
        // icId = ((GateBase) part).parentCircuit.getGateIndex((GateBase) part);
        // part = ((GateBase) part).parentCircuit;
        // }
        partId = getPartId(part);
        if (partId == -1)
            BluePower.log.warn("[MessageGuiUpdate] BPPart couldn't be found");

        this.messageId = messageId;
        this.value = value;
    }

    public MessageGuiUpdate(TileEntity tile, int messageId, int value) {

        super(tile.getPos());
        partId = -1;
        this.messageId = messageId;
        this.value = value;
    }

    private int getPartId(IMultipartTile part) {

        List<IMultipartTile> parts = MultipartHelper.getContainer(part.getWorld(), part.getPos()).getParts();
        return parts.indexOf(part);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);
        buf.writeInt(messageId);
        buf.writeInt(partId);
        buf.writeInt(value);
        buf.writeInt(icId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);
        messageId = buf.readInt();
        partId = buf.readInt();
        value = buf.readInt();
        icId = buf.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        IMultipartContainer partHolder = MultipartHelper.getContainer(player.world, new BlockPos(x, y, z)).get();
        if (partHolder != null) {
            messagePart(player, partHolder);
        } else {
            TileEntity te = player.world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof IGuiButtonSensitive) {
                ((IGuiButtonSensitive) te).onButtonPress(player, messageId, value);
            }
        }
    }

    private void messagePart(EntityPlayer player, IMultipartContainer partHolder) {

        List<IMultipartTile> parts = partHolder.getParts();
        if (partId < parts.size()) {
            IMultipartTile part = parts.get(partId);
            // IntegratedCircuit circuit = null;
            // if (part instanceof IntegratedCircuit) {
            // circuit = (IntegratedCircuit) part;
            // part = ((IntegratedCircuit) part).getPartForIndex(message.icId);
            // }
            if (part instanceof IGuiButtonSensitive) {
                ((IGuiButtonSensitive) part).onButtonPress(player, messageId, value);
                // if (circuit != null)
                // circuit.sendUpdatePacket();
            } else {
                BluePower.log.error("[BluePower][MessageGuiPacket] Part doesn't implement IGuiButtonSensitive");
            }
        }
    }
}
