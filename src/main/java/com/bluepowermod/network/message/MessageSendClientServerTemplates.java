/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.bluepowermod.tile.tier3.TileCircuitDatabase;

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
