/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.BluePower;
import com.bluepowermod.helper.ItemStackDatabase;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;

import cpw.mods.fml.common.network.ByteBufUtils;

/**
 * Used from client to server to select a template from the private library of the client.
 * Used from server to client to message the client to save the current template to the private library of the client.
 * @author MineMaarten
 */
public class MessageCircuitDatabaseTemplate extends LocationIntPacket<MessageCircuitDatabaseTemplate> {
    
    private ItemStack stack;
    private boolean   deleting; //server side only used
                                
    public MessageCircuitDatabaseTemplate() {
    
    }
    
    public MessageCircuitDatabaseTemplate(TileCircuitDatabase circuitDatabase, ItemStack stack) {
    
        super(circuitDatabase.xCoord, circuitDatabase.yCoord, circuitDatabase.zCoord);
        this.stack = stack;
    }
    
    public MessageCircuitDatabaseTemplate(TileCircuitDatabase circuitDatabase, ItemStack stack, boolean deleting) {
    
        super(circuitDatabase.xCoord, circuitDatabase.yCoord, circuitDatabase.zCoord);
        this.stack = stack;
        this.deleting = deleting;
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
        super.toBytes(buf);
        ByteBufUtils.writeItemStack(buf, stack);
        buf.writeBoolean(deleting);
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
        super.fromBytes(buf);
        stack = ByteBufUtils.readItemStack(buf);
        deleting = buf.readBoolean();
    }
    
    @Override
    public void handleClientSide(MessageCircuitDatabaseTemplate message, EntityPlayer player) {
    
        TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
        if (te instanceof TileCircuitDatabase) {
            ((TileCircuitDatabase) te).saveToPrivateLibrary(message.stack);
        }
    }
    
    @Override
    public void handleServerSide(MessageCircuitDatabaseTemplate message, EntityPlayer player) {
    
        if (message.deleting) {
            if (TileCircuitDatabase.hasPermissions(player)) {
                ItemStackDatabase stackDatabase = new ItemStackDatabase();
                stackDatabase.deleteStack(message.stack);
                NetworkHandler.sendToAll(new MessageSendClientServerTemplates(stackDatabase.loadItemStacks()));
            }
        } else {
            TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
            if (te instanceof TileCircuitDatabase) {
                ((TileCircuitDatabase) te).copyInventory.setInventorySlotContents(0, message.stack);
                player.openGui(BluePower.instance, GuiIDs.CIRCUITDATABASE_MAIN_ID.ordinal(), player.worldObj, message.x, message.y, message.z);
            }
        }
    }
    
}
