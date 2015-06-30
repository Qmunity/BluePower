package com.bluepowermod.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bluepowermod.api.misc.ICachedBlock;

public class CachedBlock implements ICachedBlock {

    private Block block;
    private int meta;
    private TileEntity tile;
    private World world;
    private int x, y, z;

    public CachedBlock(World world, int x, int y, int z) {

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public World getWorld() {

        return world;
    }

    @Override
    public int getX() {

        return x;
    }

    @Override
    public int getY() {

        return y;
    }

    @Override
    public int getZ() {

        return z;
    }

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

    @Override
    public void setBlock(Block block) {

        this.block = block;
    }

    @Override
    public void setMeta(int meta) {

        this.meta = meta;
    }

    @Override
    public void setTile(TileEntity tile) {

        this.tile = tile;
    }

    @Override
    public void clear() {

        block = Blocks.air;
        meta = 0;
        tile = null;
    }

    @Override
    public String toString() {

        return "CachedBlock [block=" + block + ", meta=" + meta + ", tile=" + tile + ", world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + "]";
    }

}
