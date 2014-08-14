package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.BluePower;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier3.TileCircuitDatabase;

import cpw.mods.fml.common.network.ByteBufUtils;

/**
 * Used from client to server to select a template from the private library of the client.
 * Used from server to client to message the client to save the current template to the private library of the client.
 * @author MineMaarten
 */
public class MessageCircuitDatabaseTemplate extends LocationIntPacket<MessageCircuitDatabaseTemplate> {
    
    private ItemStack stack;
    
    public MessageCircuitDatabaseTemplate() {
    
    }
    
    public MessageCircuitDatabaseTemplate(TileCircuitDatabase circuitDatabase, ItemStack stack) {
    
        super(circuitDatabase.xCoord, circuitDatabase.yCoord, circuitDatabase.zCoord);
        this.stack = stack;
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
        super.toBytes(buf);
        ByteBufUtils.writeItemStack(buf, stack);
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
        super.fromBytes(buf);
        stack = ByteBufUtils.readItemStack(buf);
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
    
        TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
        if (te instanceof TileCircuitDatabase) {
            ((TileCircuitDatabase) te).copyInventory.setInventorySlotContents(0, message.stack);
            player.openGui(BluePower.instance, GuiIDs.CIRCUITDATABASE_MAIN_ID.ordinal(), player.worldObj, message.x, message.y, message.z);
        }
    }
    
}
