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
