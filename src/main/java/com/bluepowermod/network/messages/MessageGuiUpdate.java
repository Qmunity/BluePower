package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.ic.IntegratedCircuit;
import com.bluepowermod.util.Dependencies;

/**
 * 
 * @author MineMaarten
 */

public class MessageGuiUpdate extends LocationIntPacket<MessageGuiUpdate> {
    
    private int partId;
    private int icId;     //only used with the Integrated Circuit
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
            if (part instanceof BPPartFace && ((BPPartFace) part).parentCircuit != null) {
                icId = ((BPPartFace) part).parentCircuit.getGateIndex((BPPartFace) part);
                part = ((BPPartFace) part).parentCircuit;
            }
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
    public void handleClientSide(MessageGuiUpdate message, EntityPlayer player) {
    
    }
    
    @Override
    public void handleServerSide(MessageGuiUpdate message, EntityPlayer player) {
    
        IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
        TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
        if (compat.isMultipart(te)) {
            messagePart(player, te, message);
        } else {
            if (te instanceof IGuiButtonSensitive) {
                ((IGuiButtonSensitive) te).onButtonPress(player, message.messageId, message.value);
            }
        }
    }
    
    private void messagePart(EntityPlayer player, TileEntity te, MessageGuiUpdate message) {
    
        List<BPPart> parts = ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).getBPParts(te, BPPart.class);
        if (message.partId < parts.size()) {
            BPPart part = parts.get(message.partId);
            IntegratedCircuit circuit = null;
            if (part instanceof IntegratedCircuit) {
                circuit = (IntegratedCircuit) part;
                part = ((IntegratedCircuit) part).getPartForIndex(message.icId);
            }
            if (part instanceof IGuiButtonSensitive) {
                ((IGuiButtonSensitive) part).onButtonPress(player, message.messageId, message.value);
                if (circuit != null) circuit.sendUpdatePacket();
            } else {
                BluePower.log.error("[BluePower][MessageGuiPacket] Part doesn't implement IGuiButtonSensitive");
            }
        }
    }
    
}
