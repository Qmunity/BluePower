package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.bluepowermod.tileentities.tier3.TileCircuitDatabase;

import cpw.mods.fml.common.network.ByteBufUtils;

public class MessageSendClientServerTemplates extends AbstractPacket<MessageSendClientServerTemplates> {
    
    private List<ItemStack> stacks;
    
    public MessageSendClientServerTemplates() {
    
    }
    
    public MessageSendClientServerTemplates(List<ItemStack> stacks) {
    
        this.stacks = stacks;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
        int amount = buf.readInt();
        stacks = new ArrayList<ItemStack>();
        for (int i = 0; i < amount; i++) {
            stacks.add(ByteBufUtils.readItemStack(buf));
        }
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
        buf.writeInt(stacks.size());
        for (ItemStack stack : stacks)
            ByteBufUtils.writeItemStack(buf, stack);
    }
    
    @Override
    public void handleClientSide(MessageSendClientServerTemplates message, EntityPlayer player) {
    
        TileCircuitDatabase.serverDatabaseStacks = message.stacks;
    }
    
    @Override
    public void handleServerSide(MessageSendClientServerTemplates message, EntityPlayer player) {
    
    }
    
}
