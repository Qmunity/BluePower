package com.bluepowermod.api.misc;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.vec.IWorldLocation;

public interface ICachedBlock extends IWorldLocation {

    public Block block();

    public int meta();

    public TileEntity tile();

    public void setBlock(Block block);

    public void setMeta(int meta);

    public void setTile(TileEntity tile);

    public void clear();

}
