package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.tileentities.BPTileMultipart;

public class MessageMultipartUpdate extends LocationIntPacket<MessageMultipartUpdate> {

    private NBTTagCompound tag;

    public MessageMultipartUpdate(NBTTagCompound tag) {

        this.tag = tag;
    }

    public MessageMultipartUpdate() {

    }

    @Override
    public void handleClientSide(MessageMultipartUpdate message, EntityPlayer player) {

        TileEntity tile = player.worldObj.getTileEntity(x, y, z);
        if (!(tile instanceof BPTileMultipart))
            return;
        ((BPTileMultipart) tile).readUpdatePacketData(tag);
    }

    @Override
    public void handleServerSide(MessageMultipartUpdate message, EntityPlayer player) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);
        writeNBT(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);
        tag = readNBT(buf);
    }

}
