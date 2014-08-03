package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.compat.fmp.IMultipartCompat;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.references.Dependencies;

/**
 * 
 * @author MineMaarten
 */

public class MessageGuiUpdate extends LocationIntPacket<MessageGuiUpdate> {
    
    private int partId;
    private int messageId;
    private int value;
    
    public MessageGuiUpdate() {
    
    }
    
    /**
     * 
     * @param part should also implement IGuiButtonSensitive to be able to receive this packet.
     * @param messageId
     * @param value
     */
    public MessageGuiUpdate(BPPart part, int messageId, int value) {
    
        super(part.getX(), part.getY(), part.getZ());
        if (part.isFMPMultipart()) {
            partId = getPartId(part);
            if (partId == -1) BluePower.log.warn("[MessageGuiUpdate] BPPart couldn't be found");
        }
        this.messageId = messageId;
        this.value = value;
    }
    
    public MessageGuiUpdate(TileEntity tile, int messageId, int value) {
    
        super(tile.xCoord, tile.yCoord, tile.zCoord);
        partId = -1;
        this.messageId = messageId;
        this.value = value;
    }
    
    private int getPartId(BPPart part) {
    
        List<BPPart> parts = ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).getBPParts(part.getWorld().getTileEntity(part.getX(), part.getY(), part.getZ()), BPPart.class);
        return parts.indexOf(part);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
        super.toBytes(buf);
        buf.writeInt(messageId);
        buf.writeInt(partId);
        buf.writeInt(value);
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
        super.fromBytes(buf);
        messageId = buf.readInt();
        partId = buf.readInt();
        value = buf.readInt();
    }
    
    @Override
    public void handleClientSide(MessageGuiUpdate message, EntityPlayer player) {
    
    }
    
    @Override
    public void handleServerSide(MessageGuiUpdate message, EntityPlayer player) {
    
        IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
        TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
        if (compat.isMultipart(te)) {
            messagePart(te, message);
        } else {
            if (te instanceof IGuiButtonSensitive) {
                ((IGuiButtonSensitive) te).onButtonPress(message.messageId, message.value);
            }
        }
    }
    
    private void messagePart(TileEntity te, MessageGuiUpdate message) {
    
        List<BPPart> parts = ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).getBPParts(te, BPPart.class);
        if (message.partId < parts.size()) {
            BPPart part = parts.get(message.partId);
            if (part instanceof IGuiButtonSensitive) {
                ((IGuiButtonSensitive) part).onButtonPress(message.messageId, message.value);
            } else {
                BluePower.log.error("[BluePower][MessageGuiPacket] Part doesn't implement IGuiButtonSensitive");
            }
        }
    }
    
}
