package com.bluepowermod.part.gate.ic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import uk.co.qmunity.lib.init.QLBlocks;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Vec3dHelper;

import javax.annotation.Nullable;
import java.io.File;


public class FakeWorldIC extends World {

    private static FakeWorldIC INSTANCE = new FakeWorldIC();

    public static FakeWorldIC getInstance() {

        return INSTANCE;
    }

    private GateIntegratedCircuit ic;

    public FakeWorldIC() {

        super(new FakeWorldSaveHandler(), null, new WorldProvider() {

            @Override
            public DimensionType getDimensionType() {
                return DimensionType.OVERWORLD;
            }

        }, new Profiler(), false);
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
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
        return false;
    }


    @Override
    public Entity getEntityByID(int p_73045_1_) {

        return null;
    }

    // Methods overriden to make this work



    @Override
    public IBlockState getBlockState(BlockPos pos) {

        try {
            if (pos.getY() == 63)
                return Blocks.STONE.getDefaultState();
            if (pos.getY() != 64)
                return Blocks.AIR.getDefaultState();

            if (ic == null || ic.getParent() == null || ic.getWorld() == null)
                return Blocks.AIR.getDefaultState();

            EnumFacing d = null;
            if (pos.getX() == -1 && pos.getZ() == ((ic.getSize() - 1) / 2))
                d = EnumFacing.WEST;
            if (pos.getX() == ic.getSize() && pos.getZ() == ((ic.getSize() - 1) / 2))
                d = EnumFacing.EAST;
            if (pos.getX() == ((ic.getSize() - 1) / 2) && pos.getZ() == -1)
                d = EnumFacing.NORTH;
            if (pos.getX() == ((ic.getSize() - 1) / 2) && pos.getZ() == ic.getSize())
                d = EnumFacing.SOUTH;
            if (d != null)
                return ic.getWorld().getBlockState(new BlockPos(Vec3dHelper.rotate(new Vec3d(new BlockPos(0, 0, 0).offset(d)), 0, 90 * -ic.getRotation(), 0).add(new Vec3d(ic.getPos()))));

            if (pos.getX() < 0 || pos.getX() >= ic.getSize() || pos.getZ() < 0 || pos.getZ() >= ic.getSize())
                return Blocks.AIR.getDefaultState();

            return QLBlocks.multipart.getDefaultState();

        } catch (Exception ex) {
        }

        return Blocks.AIR.getDefaultState();
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        try {
            if (pos.getY() != 64)
                return null;

            if (ic == null || ic.getParent() == null || ic.getWorld() == null)
                return null;

            EnumFacing d = null;
            if (pos.getX() == -1 && pos.getZ() == ((ic.getSize() - 1) / 2))
                d = EnumFacing.WEST;
            if (pos.getX() == ic.getSize() && pos.getZ() == ((ic.getSize() - 1) / 2))
                d = EnumFacing.EAST;
            if (pos.getX() == ((ic.getSize() - 1) / 2) && pos.getZ() == -1)
                d = EnumFacing.NORTH;
            if (pos.getX() == ((ic.getSize() - 1) / 2) && pos.getZ() == ic.getSize())
                d = EnumFacing.SOUTH;
            if (d != null)


                return ic.getWorld().getTileEntity(new BlockPos(Vec3dHelper.rotate(new Vec3d(new BlockPos(0, 0, 0).offset(d)), 0, 90 * -ic.getRotation(), 0).add(new Vec3d(ic.getPos()))));

            return getTile(pos.getX(), pos.getZ());
        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP;
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
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
        public IPlayerFileData getPlayerNBTManager() {
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
        public TemplateManager getStructureTemplateManager() {
            return null;
        }

    }

}
