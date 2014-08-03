package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.tileentities.BPTileMultipart;

import cpw.mods.fml.common.network.ByteBufUtils;

public class MessageMultipartUpdate extends LocationIntPacket<MessageMultipartUpdate> {
    
    private NBTTagCompound tag;
    
    public MessageMultipartUpdate(BPTileMultipart multipart, NBTTagCompound tag) {
    
        super(multipart.xCoord, multipart.yCoord, multipart.zCoord);
        this.tag = tag;
    }
    
    public MessageMultipartUpdate() {
    
    }
    
    @Override
    public void handleClientSide(MessageMultipartUpdate message, EntityPlayer player) {
    
        TileEntity tile = player.worldObj.getTileEntity(message.x, message.y, message.z);
        if (!(tile instanceof BPTileMultipart)) return;
        ((BPTileMultipart) tile).readUpdatePacketData(message.tag);
    }
    
    @Override
    public void handleServerSide(MessageMultipartUpdate message, EntityPlayer player) {
    
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
        super.toBytes(buf);
        ByteBufUtils.writeTag(buf, tag);
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
        super.fromBytes(buf);
        tag = ByteBufUtils.readTag(buf);
    }
}
