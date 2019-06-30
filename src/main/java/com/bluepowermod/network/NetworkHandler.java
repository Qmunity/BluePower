package com.bluepowermod.network;

import net.minecraftforge.api.distmarker.Dist;

public class NetworkHandler{

    //public final SimpleNetworkWrapper wrapper;
    private int lastDiscriminator = 0;

    public NetworkHandler(String modid){

        //wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(modid);

    }

    public static void initQLib(){

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void registerPacket(Class packetHandler, Class packetType, Dist side){

        //wrapper.registerMessage(packetHandler, packetType, lastDiscriminator++, side);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void registerPacket(Class packetType, Dist side){

        //wrapper.registerMessage(packetType, packetType, lastDiscriminator++, side);
    }



}
