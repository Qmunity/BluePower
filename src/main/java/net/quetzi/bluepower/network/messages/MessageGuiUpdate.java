package net.quetzi.bluepower.network.messages;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.compat.CompatibilityUtils;
import net.quetzi.bluepower.compat.fmp.IMultipartCompat;
import net.quetzi.bluepower.compat.fmp.MultipartBPPart;
import net.quetzi.bluepower.part.IGuiButtonSensitive;
import net.quetzi.bluepower.references.Dependencies;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.common.Optional;

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
    
        super(part.x, part.y, part.z);
        if (part.isFMPMultipart()) {
            partId = getPartId(part);
        }
        this.messageId = messageId;
        this.value = value;
    }
    
    @Optional.Method(modid = Dependencies.FMP)
    private int getPartId(BPPart part) {
    
        List<TMultiPart> parts = ((TileMultipart) part.world.getTileEntity(part.x, part.y, part.z)).jPartList();
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i) instanceof MultipartBPPart) {
                if (((MultipartBPPart) parts.get(i)).getPart() == part) return i;
            }
        }
        throw new IllegalArgumentException("[BluePower][MessageGuiPacket] No part found for sent GUI packet");
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
        }
    }
    
    @Optional.Method(modid = Dependencies.FMP)
    private void messagePart(TileEntity te, MessageGuiUpdate message) {
    
        List<TMultiPart> parts = ((TileMultipart) te).jPartList();
        if (message.partId < parts.size()) {
            TMultiPart part = parts.get(message.partId);
            if (part instanceof MultipartBPPart) {
                if (((MultipartBPPart) part).getPart() instanceof IGuiButtonSensitive) {
                    ((IGuiButtonSensitive) ((MultipartBPPart) part).getPart()).onButtonPress(message.messageId, message.value);
                } else {
                    BluePower.log.error("[BluePower][MessageGuiPacket] Part doesn't implement IGuiButtonSensitive");
                }
            }
        }
    }
    
}
