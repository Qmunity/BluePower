package com.bluepowermod.part.gate.ic;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.util.EnumFacing;;
import uk.co.qmunity.lib.init.QLBlocks;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Vec3d;

public class FakeWorldIC extends World {

    private static FakeWorldIC INSTANCE = new FakeWorldIC();

    public static FakeWorldIC getInstance() {

        return INSTANCE;
    }

    private GateIntegratedCircuit ic;

    public FakeWorldIC() {

        super(new FakeWorldSaveHandler(), "IC_Fake_World", null, new WorldProvider() {

            @Override
            public String getDimensionName() {

                return "IC_Fake_World";
            }
        }, new Profiler());
    }

    public void setIC(GateIntegratedCircuit ic) {

        this.ic = ic;
    }

    public GateIntegratedCircuit getIC() {

        return ic;
    }

    private TileMultipart getTile(int x, int z) {

        GateIntegratedCircuit ic = getIC();

        if (ic == null)
            return null;

        return ic.getTile(x, z);
    }

    @Override
    protected IChunkProvider createChunkProvider() {

        return null;
    }

    @Override
    protected int func_152379_p() {

        return 0;
    }

    @Override
    public Entity getEntityByID(int p_73045_1_) {

        return null;
    }

    // Methods overriden to make this work

    @Override
    public Block getBlock(int x, int y, int z) {

        try {
            if (y == 63)
                return Blocks.stone;
            if (y != 64)
                return Blocks.air;

            if (ic == null || ic.getParent() == null || ic.getWorld() == null)
                return Blocks.air;

            EnumFacing d = null;
            if (x == -1 && z == ((ic.getSize() - 1) / 2))
                d = EnumFacing.WEST;
            if (x == ic.getSize() && z == ((ic.getSize() - 1) / 2))
                d = EnumFacing.EAST;
            if (x == ((ic.getSize() - 1) / 2) && z == -1)
                d = EnumFacing.NORTH;
            if (x == ((ic.getSize() - 1) / 2) && z == ic.getSize())
                d = EnumFacing.SOUTH;
            if (d != null) {
                return new Vec3d(0, 0, 0, ic.getWorld()).add(d).rotate(0, 90 * -ic.getRotation(), 0).add(ic.getX(), ic.getY(), ic.getZ())
                        .getBlock();
            }

            if (x < 0 || x >= ic.getSize() || z < 0 || z >= ic.getSize())
                return Blocks.air;

            return QLBlocks.multipart;
        } catch (Exception ex) {
        }

        return Blocks.air;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {

        try {
            if (ic == null || ic.getParent() == null || ic.getWorld() == null)
                return 0;

            EnumFacing d = null;
            if (x == -1 && z == ((ic.getSize() - 1) / 2))
                d = EnumFacing.WEST;
            if (x == ic.getSize() && z == ((ic.getSize() - 1) / 2))
                d = EnumFacing.EAST;
            if (x == ((ic.getSize() - 1) / 2) && z == -1)
                d = EnumFacing.NORTH;
            if (x == ((ic.getSize() - 1) / 2) && z == ic.getSize())
                d = EnumFacing.SOUTH;
            if (d != null)
                return new Vec3d(0, 0, 0, ic.getWorld()).add(d).rotate(0, 90 * -ic.getRotation(), 0).add(ic.getX(), ic.getY(), ic.getZ())
                        .getBlockMeta();
        } catch (Exception ex) {
        }

        return 0;
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {

        try {
            if (y != 64)
                return null;

            if (ic == null || ic.getParent() == null || ic.getWorld() == null)
                return null;

            EnumFacing d = null;
            if (x == -1 && z == ((ic.getSize() - 1) / 2))
                d = EnumFacing.WEST;
            if (x == ic.getSize() && z == ((ic.getSize() - 1) / 2))
                d = EnumFacing.EAST;
            if (x == ((ic.getSize() - 1) / 2) && z == -1)
                d = EnumFacing.NORTH;
            if (x == ((ic.getSize() - 1) / 2) && z == ic.getSize())
                d = EnumFacing.SOUTH;
            if (d != null)
                return new Vec3d(0, 0, 0, ic.getWorld()).add(d).rotate(0, 90 * -ic.getRotation(), 0).add(ic.getX(), ic.getY(), ic.getZ())
                        .getTileEntity();

            return getTile(x, z);
        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    public void markTileEntityChunkModified(int p_147476_1_, int p_147476_2_, int p_147476_3_, TileEntity p_147476_4_) {

    }

    @Override
    public void func_147453_f(int p_147453_1_, int p_147453_2_, int p_147453_3_, Block p_147453_4_) {

    }

    @Override
    public boolean isSideSolid(int x, int y, int z, EnumFacing side) {

        return side == EnumFacing.UP;
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, EnumFacing side, boolean _default) {

        return side == EnumFacing.UP;
    }

    private static class FakeWorldSaveHandler implements ISaveHandler {

        @Override
        public WorldInfo loadWorldInfo() {

            return new WorldInfo(new NBTTagCompound());
        }

        @Override
        public void checkSessionLock() throws MinecraftException {

        }

        @Override
        public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {

            return null;
        }

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {

        }

        @Override
        public void saveWorldInfo(WorldInfo p_75761_1_) {

        }

        @Override
        public IPlayerFileData getSaveHandler() {

            return null;
        }

        @Override
        public void flush() {

        }

        @Override
        public File getWorldDirectory() {

            return null;
        }

        @Override
        public File getMapFileFromName(String p_75758_1_) {

            return null;
        }

        @Override
        public String getWorldDirectoryName() {

            return null;
        }

    }

}
