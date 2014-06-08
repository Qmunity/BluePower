/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ExampleMessage implements IMessage, IMessageHandler<ExampleMessage, IMessage> {
    
    private int data;
    
    public ExampleMessage() {
    
    }
    
    public ExampleMessage(int data) {
    
        this.data = data;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
        data = buf.readInt();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
        buf.writeInt(data);
    }
    
    @SuppressWarnings("unused")
    @Override
    public IMessage onMessage(ExampleMessage message, MessageContext ctx) {
    
        int ourData = message.data;
        
        /**
         * Return a message here if we want to reply to the sender
         */
        return null;
    }
}
