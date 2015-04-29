package com.bluepowermod.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.api.misc.ICachedBlock;

public class CachedBlock implements ICachedBlock {

    private Block block;
    private int meta;
    private TileEntity tile;

    @Override
    public Block block() {

        return block;
    }

    @Override
    public int meta() {

        return meta;
    }

    @Override
    public TileEntity tile() {

        return tile;
    }

    public void setBlock(Block block) {

        this.block = block;
    }

    public void setMeta(int meta) {

        this.meta = meta;
    }

    public void setTile(TileEntity tile) {

        this.tile = tile;
    }

    public void clear() {

        block = Blocks.air;
        meta = 0;
        tile = null;
    }

}
