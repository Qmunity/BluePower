package com.bluepowermod.api.bluestone;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.vec.Vector3;
import com.jcraft.jorbis.Block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBluestoneApi {

    public void registerSpecialConnection(Block block, int metadata, ForgeDirection cableSide, int extraLength);

    public void registerSpecialConnection(TileEntity te, ForgeDirection cableSide, int extraLength);

    public void registerSpecialConnection(ABluestoneConnect connection);

    public int getExtraLength(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide);

    public boolean canConnect(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide);

    @SideOnly(Side.CLIENT)
    public void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz);

    @SideOnly(Side.CLIENT)
    public void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz, int textureSize);

    public void renderExtraCables(Vector3 block, IBluestoneWire wire, ForgeDirection side);

}
