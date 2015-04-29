package com.bluepowermod.part.gate.ic;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3d;

import com.bluepowermod.util.CachedBlock;

public class FakeWorldIC extends World {

    private static FakeWorldIC INSTANCE = new FakeWorldIC();

    public static FakeWorldIC getInstance() {

        return INSTANCE;
    }

    private GateIntegratedCircuit ic;
    private Chunk[][] chunks = new Chunk[3][3];

    public FakeWorldIC() {

        super(new FakeWorldSaveHandler(), "IC_Fake_World", null, new WorldProvider() {

            @Override
            public String getDimensionName() {

                return "IC_Fake_World";
            }
        }, new Profiler());

        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                chunks[x][z] = new Chunk(this, x - 1, z - 1) {

                    @Override
                    public boolean func_150807_a(int x, int y, int z, Block b, int meta) {

                        setBlock(xPosition * 16 + x, y, zPosition * 16 + z, b, meta, 3);
                        return true;
                    }

                    @Override
                    public void func_150812_a(int x, int y, int z, TileEntity te) {

                        te.setWorldObj(worldObj);
                        setTileEntity(te.xCoord = xPosition * 16 + x, te.yCoord = y, te.zCoord = zPosition * 16 + z, te);
                    }
                };
            }
        }
    }

    public void setIC(GateIntegratedCircuit ic) {

        this.ic = ic;
    }

    public GateIntegratedCircuit getIC() {

        return ic;
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

            ForgeDirection d = null;
            if (x == -1 && z == ((ic.getSize() - 1) / 2))
                d = ForgeDirection.WEST;
            if (x == ic.getSize() && z == ((ic.getSize() - 1) / 2))
                d = ForgeDirection.EAST;
            if (x == ((ic.getSize() - 1) / 2) && z == -1)
                d = ForgeDirection.NORTH;
            if (x == ((ic.getSize() - 1) / 2) && z == ic.getSize())
                d = ForgeDirection.SOUTH;
            if (d != null)
                return new Vec3d(0, 0, 0, ic.getWorld()).add(d).rotate(0, 90 * -ic.getRotation(), 0).add(ic.getX(), ic.getY(), ic.getZ())
                        .getBlock();

            if (x < 0 || x >= ic.getSize() || z < 0 || z >= ic.getSize())
                return Blocks.air;

            return ic.getBlock(x, z).block();
        } catch (Exception ex) {
        }

        return Blocks.air;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {

        try {
            if (ic == null || ic.getParent() == null || ic.getWorld() == null)
                return 0;

            ForgeDirection d = null;
            if (x == -1 && z == ((ic.getSize() - 1) / 2))
                d = ForgeDirection.WEST;
            if (x == ic.getSize() && z == ((ic.getSize() - 1) / 2))
                d = ForgeDirection.EAST;
            if (x == ((ic.getSize() - 1) / 2) && z == -1)
                d = ForgeDirection.NORTH;
            if (x == ((ic.getSize() - 1) / 2) && z == ic.getSize())
                d = ForgeDirection.SOUTH;
            if (d != null)
                return new Vec3d(0, 0, 0, ic.getWorld()).add(d).rotate(0, 90 * -ic.getRotation(), 0).add(ic.getX(), ic.getY(), ic.getZ())
                        .getBlockMeta();

            return ic.getBlock(x, z).meta();
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

            ForgeDirection d = null;
            if (x == -1 && z == ((ic.getSize() - 1) / 2))
                d = ForgeDirection.WEST;
            if (x == ic.getSize() && z == ((ic.getSize() - 1) / 2))
                d = ForgeDirection.EAST;
            if (x == ((ic.getSize() - 1) / 2) && z == -1)
                d = ForgeDirection.NORTH;
            if (x == ((ic.getSize() - 1) / 2) && z == ic.getSize())
                d = ForgeDirection.SOUTH;
            if (d != null)
                return new Vec3d(0, 0, 0, ic.getWorld()).add(d).rotate(0, 90 * -ic.getRotation(), 0).add(ic.getX(), ic.getY(), ic.getZ())
                        .getTileEntity();

            return ic.getBlock(x, z).tile();
        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block b, int meta, int update) {

        if (x < 0 || z < 0 || x >= ic.getSize() || z >= ic.getSize())
            return false;
        if (y != 64)
            return false;

        CachedBlock cb = ic.getOrCreateBlock(x, z);
        cb.setBlock(b);
        cb.setMeta(meta);

        return true;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int update) {

        if (x < 0 || z < 0 || x >= ic.getSize() || z >= ic.getSize())
            return false;
        if (y != 64)
            return false;

        ic.getOrCreateBlock(x, z).setMeta(meta);
        return true;
    }

    @Override
    public void setTileEntity(int x, int y, int z, TileEntity te) {

        if (x < 0 || z < 0 || x >= ic.getSize() || z >= ic.getSize())
            return;
        if (y != 64)
            return;

        ic.getOrCreateBlock(x, z).setTile(te);
    }

    @Override
    public void markTileEntityChunkModified(int p_147476_1_, int p_147476_2_, int p_147476_3_, TileEntity p_147476_4_) {

    }

    @Override
    public void func_147453_f(int p_147453_1_, int p_147453_2_, int p_147453_3_, Block p_147453_4_) {

    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side) {

        return y != 64 && side == ForgeDirection.UP;
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {

        return y != 64 ? _default : side == ForgeDirection.UP;
    }

    @Override
    protected boolean chunkExists(int p_72916_1_, int p_72916_2_) {

        return true;
    }

    @Override
    public boolean checkNoEntityCollision(AxisAlignedBB p_72917_1_, Entity p_72917_2_) {

        return true;
    }

    @Override
    public Chunk getChunkFromChunkCoords(int x, int z) {

        return chunks[x + 1][z + 1];
    }

    @Override
    public boolean checkNoEntityCollision(AxisAlignedBB p_72855_1_) {

        return true;
    }

    @Override
    public MovingObjectPosition func_147447_a(Vec3 start, Vec3 end, boolean fluids, boolean unknown1, boolean unknown2) {

        Vec3 s = new Vec3d(start).sub(ic.getX(), ic.getY(), ic.getZ())
                .sub(GateIntegratedCircuit.border, GateIntegratedCircuit.border, GateIntegratedCircuit.border)
                .div(1 - 2 * GateIntegratedCircuit.border).mul(ic.getSize()).add(0, 64, 0).toVec3();
        Vec3 e = new Vec3d(end).sub(ic.getX(), ic.getY(), ic.getZ())
                .sub(GateIntegratedCircuit.border, GateIntegratedCircuit.border, GateIntegratedCircuit.border)
                .div(1 - 2 * GateIntegratedCircuit.border).mul(ic.getSize()).add(0, 64, 0).toVec3();

        return super.func_147447_a(s, e, fluids, unknown1, unknown2);
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
