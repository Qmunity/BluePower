package com.bluepowermod.api.misc;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public interface ICachedBlock {

    public Block block();

    public int meta();

    public TileEntity tile();

}
