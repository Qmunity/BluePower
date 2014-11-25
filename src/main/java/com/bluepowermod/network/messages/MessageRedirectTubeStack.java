package com.bluepowermod.network.messages;

import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.part.tube.TubeLogic;
import com.bluepowermod.part.tube.TubeStack;

public class MessageRedirectTubeStack extends LocationIntPacket<MessageRedirectTubeStack> {
    private TubeStack stack;

    public MessageRedirectTubeStack() {
    }

    public MessageRedirectTubeStack(PneumaticTube tube, TubeStack stack) {
        super(tube.getX(), tube.getY(), tube.getZ());
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        stack.writeToPacket(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        stack = TubeStack.loadFromPacket(buf);
    }

    @Override
    public void handleClientSide(MessageRedirectTubeStack message, EntityPlayer player) {
        TubeLogic logic = MultipartCompatibility.getPart(player.worldObj, message.x, message.y, message.z, PneumaticTube.class).getLogic();
        logic.onClientTubeRedirectPacket(message.stack);
    }

    @Override
    public void handleServerSide(MessageRedirectTubeStack message, EntityPlayer player) {
    }

}
