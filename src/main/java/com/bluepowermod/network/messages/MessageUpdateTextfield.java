/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
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
