package com.bluepowermod.api.bluestone;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.vec.Vector3;
import com.jcraft.jorbis.Block;

public interface IBluestoneApi {

    public void registerSpecialConnection(Block block, int metadata, ForgeDirection cableSide, int extraLength);

    public void registerSpecialConnection(TileEntity te, ForgeDirection cableSide, int extraLength);

    public void registerSpecialConnection(IBluestoneConnect connection);

    public static interface IBluestoneConnect {

        public int getExtraLength(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide);

        public boolean canConnect(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide);

    }

    public int getExtraLength(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide);

    boolean canConnect(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide);

}
