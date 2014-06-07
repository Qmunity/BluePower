package net.quetzi.bluepower.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.quetzi.bluepower.network.messages.ExampleMessage;
import net.quetzi.bluepower.references.Refs;

public class NetworkHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Refs.MODID.toLowerCase());

    /*
    The integer is the ID of the message, the Side is the side this message will be handled (received) on!
     */
    public static void init()
    {
        INSTANCE.registerMessage(ExampleMessage.class, ExampleMessage.class, 0, Side.SERVER);
    }
}
