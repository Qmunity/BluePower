package com.bluepowermod.part.gate.ic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.init.QLBlocks;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartUpdateListener;
import uk.co.qmunity.lib.util.Dir;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.helper.IOHelper;

public class FakeWorldIC extends World {

    private static Map<World, FakeWorldIC> worlds = new HashMap<World, FakeWorldIC>();

    public static FakeWorldIC load(GateIntegratedCircuit ic) {

        FakeWorldIC w = worlds.containsKey(ic.getWorld()) ? worlds.get(ic.getWorld()) : null;
        if (w == null)
            worlds.put(ic.getWorld(), w = new FakeWorldIC(ic.getWorld()));
        w.ic = ic;
        if (ic.getWorld() != null)
            w.isRemote = ic.getWorld().isRemote;
        else
            w.isRemote = false;
        return w;
    }

    private GateIntegratedCircuit ic;

    public FakeWorldIC(World world) {

        super(new FakeWorldSaveHandler(), "IC_Fake_World", null, new WorldProvider() {

            @Override
            public String getDimensionName() {

                return "IC_Fake_World";
            }
        }, new Profiler());
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
    public Block getBlock(int x, int y, int z) {

        if (y <= 63)
            return Blocks.stone;
        if (y == 64) {
            if (x >= 0 && z >= 0 && x < ic.getSize() && z < ic.getSize())
                return ic.tiles[x][z] != null ? QLBlocks.multipart : Blocks.air;
            if (z == -1) {
                Vec3i vec = new Vec3i(ic).add(Dir.FRONT.toForgeDirection(ic.getFace(), ic.getRotation()));
                TileEntity te = vec.getTileEntity();
                // if (x >= 0 && x < ic.getSize() && te != null && te instanceof TileMultipart)
                // for (IPart p : ((TileMultipart) te).getParts())
                // if (p instanceof GateIntegratedCircuit && ((GateIntegratedCircuit) p).getFace() == ic.getFace())
                // return QLBlocks.multipart;
                if (x == (ic.getSize() - 1) / 2)
                    return vec.getBlock();
            }
            if (z == ic.getSize()) {
                Vec3i vec = new Vec3i(ic).add(Dir.BACK.toForgeDirection(ic.getFace(), ic.getRotation()));
                // TileEntity te = vec.getTileEntity();
                // if (x >= 0 && x < ic.getSize() && te != null && te instanceof TileMultipart)
                // for (IPart p : ((TileMultipart) te).getParts())
                // if (p instanceof GateIntegratedCircuit && ((GateIntegratedCircuit) p).getFace() == ic.getFace())
                // return QLBlocks.multipart;
                if (x == (ic.getSize() - 1) / 2)
                    return vec.getBlock();
            }
            if (x == -1) {
                Vec3i vec = new Vec3i(ic).add(Dir.LEFT.toForgeDirection(ic.getFace(), ic.getRotation()));
                // TileEntity te = vec.getTileEntity();
                // if (z >= 0 && z < ic.getSize() && te != null && te instanceof TileMultipart)
                // for (IPart p : ((TileMultipart) te).getParts())
                // if (p instanceof GateIntegratedCircuit && ((GateIntegratedCircuit) p).getFace() == ic.getFace())
                // return QLBlocks.multipart;
                if (z == (ic.getSize() - 1) / 2)
                    return vec.getBlock();
            }
            if (x == ic.getSize()) {
                Vec3i vec = new Vec3i(ic).add(Dir.RIGHT.toForgeDirection(ic.getFace(), ic.getRotation()));
                // TileEntity te = vec.getTileEntity();
                // if (z >= 0 && z < ic.getSize() && te != null && te instanceof TileMultipart)
                // for (IPart p : ((TileMultipart) te).getParts())
                // if (p instanceof GateIntegratedCircuit && ((GateIntegratedCircuit) p).getFace() == ic.getFace())
                // return QLBlocks.multipart;
                if (z == (ic.getSize() - 1) / 2)
                    return vec.getBlock();
            }
        }

        return Blocks.air;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {

        if (y == 64) {
            if (x >= 0 && z >= 0 && x < ic.getSize() && z < ic.getSize())
                return 0;
            if (x == (ic.getSize() - 1) / 2 && z == -1)
                return new Vec3i(ic).add(Dir.FRONT.toForgeDirection(ic.getFace(), ic.getRotation())).getBlockMeta();
            if (x == (ic.getSize() - 1) / 2 && z == ic.getSize())
                return new Vec3i(ic).add(Dir.BACK.toForgeDirection(ic.getFace(), ic.getRotation())).getBlockMeta();
            if (x == -1 && z == (ic.getSize() - 1) / 2)
                return new Vec3i(ic).add(Dir.LEFT.toForgeDirection(ic.getFace(), ic.getRotation())).getBlockMeta();
            if (x == ic.getSize() && z == (ic.getSize() - 1) / 2)
                return new Vec3i(ic).add(Dir.RIGHT.toForgeDirection(ic.getFace(), ic.getRotation())).getBlockMeta();
        }
        return 0;
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {

        if (y == 64) {
            if (x >= 0 && z >= 0 && x < ic.getSize() && z < ic.getSize())
                return ic.tiles[x][z];
            if (z == -1) {
                TileEntity te = new Vec3i(ic).add(Dir.FRONT.toForgeDirection(ic.getFace(), ic.getRotation())).getTileEntity();
                // if (x >= 0 && x < ic.getSize() && te != null && te instanceof TileMultipart) {
                // for (IPart p : ((TileMultipart) te).getParts()) {
                // if (p instanceof GateIntegratedCircuit && ((GateIntegratedCircuit) p).getFace() == ic.getFace()) {
                // int v = ((((GateIntegratedCircuit) p).getSize() - 1) / 2) - ((ic.getSize() - 1) / 2) + x;
                // if (v < 0 || v >= ((GateIntegratedCircuit) p).getSize())
                // return null;
                // return ((GateIntegratedCircuit) p).tiles[v][((GateIntegratedCircuit) p).getSize() - 1];
                // }
                // }
                // }
                if (x == (ic.getSize() - 1) / 2)
                    return te;
            }
            if (z == ic.getSize()) {
                TileEntity te = new Vec3i(ic).add(Dir.BACK.toForgeDirection(ic.getFace(), ic.getRotation())).getTileEntity();
                // if (x >= 0 && x < ic.getSize() && te != null && te instanceof TileMultipart) {
                // for (IPart p : ((TileMultipart) te).getParts()) {
                // if (p instanceof GateIntegratedCircuit && ((GateIntegratedCircuit) p).getFace() == ic.getFace()) {
                // int v = ((((GateIntegratedCircuit) p).getSize() - 1) / 2) - ((ic.getSize() - 1) / 2) + x;
                // if (v < 0 || v >= ((GateIntegratedCircuit) p).getSize())
                // return null;
                // return ((GateIntegratedCircuit) p).tiles[v][0];
                // }
                // }
                // }
                if (x == (ic.getSize() - 1) / 2)
                    return te;
            }
            if (x == -1) {
                TileEntity te = new Vec3i(ic).add(Dir.LEFT.toForgeDirection(ic.getFace(), ic.getRotation())).getTileEntity();
                // if (z >= 0 && z < ic.getSize() && te != null && te instanceof TileMultipart) {
                // for (IPart p : ((TileMultipart) te).getParts()) {
                // if (p instanceof GateIntegratedCircuit && ((GateIntegratedCircuit) p).getFace() == ic.getFace()) {
                // int v = ((((GateIntegratedCircuit) p).getSize() - 1) / 2) - ((ic.getSize() - 1) / 2) + z;
                // if (v < 0 || v >= ((GateIntegratedCircuit) p).getSize())
                // return null;
                // return ((GateIntegratedCircuit) p).tiles[((GateIntegratedCircuit) p).getSize() - 1][v];
                // }
                // }
                // }
                if (z == (ic.getSize() - 1) / 2)
                    return te;
            }
            if (x == ic.getSize()) {
                TileEntity te = new Vec3i(ic).add(Dir.RIGHT.toForgeDirection(ic.getFace(), ic.getRotation())).getTileEntity();
                // if (z >= 0 && z < ic.getSize() && te != null && te instanceof TileMultipart) {
                // for (IPart p : ((TileMultipart) te).getParts()) {
                // if (p instanceof GateIntegratedCircuit && ((GateIntegratedCircuit) p).getFace() == ic.getFace()) {
                // int v = ((((GateIntegratedCircuit) p).getSize() - 1) / 2) - ((ic.getSize() - 1) / 2) + z;
                // if (v < 0 || v >= ((GateIntegratedCircuit) p).getSize())
                // return null;
                // return ((GateIntegratedCircuit) p).tiles[0][v];
                // }
                // }
                // }
                if (z == (ic.getSize() - 1) / 2)
                    return te;
            }
        }

        return null;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block) {

        return false;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int notify) {

        return false;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int notify) {

        return false;
    }

    @Override
    public boolean setBlockToAir(int x, int y, int z) {

        return false;
    }

    @Override
    public void setTileEntity(int x, int y, int z, TileEntity te) {

    }

    @Override
    public void removeTileEntity(int x, int y, int z) {

    }

    @Override
    public int getBlockLightValue(int p_72957_1_, int p_72957_2_, int p_72957_3_) {

        return ic.getWorld().getBlockLightValue(p_72957_1_, p_72957_2_, p_72957_3_);
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int unknown) {

        return ic.getWorld().getLightBrightnessForSkyBlocks(x, y, z, unknown);
    }

    @Override
    public int isBlockProvidingPowerTo(int x, int y, int z, int face) {

        return 0;
    }

    @Override
    public Entity getEntityByID(int id) {

        return ic.getWorld().getEntityByID(id);
    }

    @Override
    public boolean spawnEntityInWorld(Entity entity) {

        if (!(entity instanceof EntityItem))
            return false;

        IOHelper.spawnItemInWorld(ic.getWorld(), ((EntityItem) entity).getEntityItem(), ic.getX(), ic.getY(), ic.getZ());
        return true;
    }

    @Override
    public void spawnParticle(String type, double x, double y, double z, double r, double g, double b) {

    }

    @Override
    public void tick() {

    }

    @Override
    public void updateEntities() {

    }

    @Override
    public void playSound(double x, double y, double z, String sound, float volume, float pitch, boolean unknown) {

        ic.getWorld().playSound(ic.getX(), ic.getY(), ic.getZ(), sound, volume, pitch, unknown);
    }

    @Override
    public void playSoundAtEntity(Entity entity, String sound, float volume, float pitch) {

        ic.getWorld().playSoundAtEntity(entity, sound, volume, pitch);
    }

    @Override
    public void playSoundEffect(double x, double y, double z, String sound, float volume, float pitch) {

        ic.getWorld().playSoundEffect(ic.getX(), ic.getY(), ic.getZ(), sound, volume, pitch);
    }

    @Override
    public void playAuxSFX(int x, int y, int z, int a, int b) {

        ic.getWorld().playAuxSFX(ic.getX(), ic.getY(), ic.getZ(), a, b);
    }

    @Override
    public void playAuxSFXAtEntity(EntityPlayer player, int x, int y, int z, int a, int b) {

        ic.getWorld().playAuxSFXAtEntity(player, ic.getX(), ic.getY(), ic.getZ(), a, b);
    }

    @Override
    public void playSoundToNearExcept(EntityPlayer player, String sound, float volume, float pitch) {

        ic.getWorld().playSoundToNearExcept(player, sound, volume, pitch);
    }

    @Override
    public void playBroadcastSound(int x, int y, int z, int p_82739_4_, int p_82739_5_) {

        ic.getWorld().playBroadcastSound(ic.getX(), ic.getY(), ic.getZ(), p_82739_4_, p_82739_5_);
    }

    @Override
    public boolean updateLightByType(EnumSkyBlock p_147463_1_, int p_147463_2_, int p_147463_3_, int p_147463_4_) {

        return false;
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side) {

        return y == 63 && side == ForgeDirection.UP;
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {

        return isSideSolid(x, y, z, side);
    }

    @Override
    public void notifyBlockChange(int x, int y, int z, Block b) {

        super.notifyBlockChange(x, y, z, b);
    }

    @Override
    public void notifyBlockOfNeighborChange(int x, int y, int z, Block b) {

        if (isRemote)
            return;

        try {
            if (y == 64 && x >= 0 && z >= 0 && x < ic.getSize() && z < ic.getSize())
                if (ic.tiles[x][z] != null)
                    for (IPart p : ic.tiles[x][z].getParts())
                        if (p instanceof IPartUpdateListener)
                            ((IPartUpdateListener) p).onNeighborBlockChange();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyBlocksOfNeighborChange(int p_147441_1_, int p_147441_2_, int p_147441_3_, Block p_147441_4_, int p_147441_5_) {

        super.notifyBlocksOfNeighborChange(p_147441_1_, p_147441_2_, p_147441_3_, p_147441_4_, p_147441_5_);
    }

    @Override
    public void notifyBlocksOfNeighborChange(int p_147459_1_, int p_147459_2_, int p_147459_3_, Block p_147459_4_) {

        super.notifyBlocksOfNeighborChange(p_147459_1_, p_147459_2_, p_147459_3_, p_147459_4_);
    }

    @Override
    public void joinEntityInSurroundings(Entity entity) {

        ic.getWorld().joinEntityInSurroundings(entity);
    }

    @Override
    public void removeEntity(Entity p_72900_1_) {

        ic.getWorld().removeEntity(p_72900_1_);
    }

    @Override
    public void markBlockForUpdate(int x, int y, int z) {

    }

    @Override
    public long getWorldTime() {

        return ic.getWorld().getWorldTime();
    }

    @Override
    public long getTotalWorldTime() {

        return ic.getWorld().getTotalWorldTime();
    }

    @Override
    public boolean isBlockNormalCubeDefault(int x, int y, int z, boolean whatever) {

        return getBlock(x, y, z).isNormalCube(this, x, y, z);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getEntitiesWithinAABB(Class p_72872_1_, AxisAlignedBB p_72872_2_) {

        return new ArrayList();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getEntitiesWithinAABBExcludingEntity(Entity p_72839_1_, AxisAlignedBB p_72839_2_) {

        return new ArrayList();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getEntitiesWithinAABBExcludingEntity(Entity p_94576_1_, AxisAlignedBB p_94576_2_, net.minecraft.command.IEntitySelector p_94576_3_) {

        return new ArrayList();
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
