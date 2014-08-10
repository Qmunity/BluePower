package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.tileentities.IGUITextFieldSensitive;

import cpw.mods.fml.common.network.ByteBufUtils;

public class MessageUpdateTextfield extends LocationIntPacket<MessageUpdateTextfield> {
    
    private int    textFieldID;
    private String text;
    
    public MessageUpdateTextfield() {
    
    }
    
    public MessageUpdateTextfield(TileEntity te, int textfieldID) {
    
        super(te.xCoord, te.yCoord, te.zCoord);
        textFieldID = textfieldID;
        text = ((IGUITextFieldSensitive) te).getText(textfieldID);
    }
    
    @Override
    public void toBytes(ByteBuf buffer) {
    
        super.toBytes(buffer);
        buffer.writeInt(textFieldID);
        ByteBufUtils.writeUTF8String(buffer, text);
    }
    
    @Override
    public void fromBytes(ByteBuf buffer) {
    
        super.fromBytes(buffer);
        textFieldID = buffer.readInt();
        text = ByteBufUtils.readUTF8String(buffer);
    }
    
    @Override
    public void handleClientSide(MessageUpdateTextfield message, EntityPlayer player) {
    
    }
    
    @Override
    public void handleServerSide(MessageUpdateTextfield message, EntityPlayer player) {
    
        TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
        if (te instanceof IGUITextFieldSensitive) {
            ((IGUITextFieldSensitive) te).setText(message.textFieldID, message.text);
        }
    }
    
}
