package com.bluepowermod.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.client.renderers.RenderMultipart;
import com.bluepowermod.raytrace.RayTracer;
import com.bluepowermod.tileentities.BPTileMultipart;
import com.bluepowermod.util.AABBUtils;
import com.bluepowermod.util.ComparatorMOP;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BPBlockMultipart extends BlockContainer {

    public BPBlockMultipart() {

        super(Material.rock);
        // MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new BPTileMultipart();
    }

    private BPTileMultipart getTile(IBlockAccess w, int x, int y, int z) {

        TileEntity tile = w.getTileEntity(x, y, z);
        if (tile == null)
            return null;
        if (tile instanceof BPTileMultipart)
            return ((BPTileMultipart) tile);
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB unused, List l, Entity entity) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return;

        for (AxisAlignedBB aabb : t.getCollisionBoxes()) {
            aabb.minX += x;
            aabb.minY += y;
            aabb.minZ += z;
            aabb.maxX += x;
            aabb.maxY += y;
            aabb.maxZ += z;
            if (aabb.intersectsWith(unused))
                l.add(aabb);
        }
    }

    @Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {

        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess w, int x, int y, int z, int side) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return false;

        return super.canConnectRedstone(w, x, y, z, side);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {

        return false;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 start, Vec3 end) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            rayTrace(w, x, y, z, RayTracer.getStartVector(player), RayTracer.getEndVector(player), t.getSelectionBoxes());
        }

        return super.collisionRayTrace(w, x, y, z, start, end);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World w, int x, int y, int z) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return -1;
        return t.getHardness(player.rayTrace(player.capabilities.isCreativeMode ? 5 : 4.5, 0), player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World w, int x, int y, int z, int metadata, int fortune) {

        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return items;

        for (BPPart p : t.getParts()) {
            List<ItemStack> i = p.getDrops();
            if (i != null)
                items.addAll(i);
        }

        return items;
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World w, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return -1;
        return t.getExplosionResistance();
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {

        return 0;
    }

    @Override
    public int getLightValue(IBlockAccess w, int x, int y, int z) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return -1;
        return t.getLightValue();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World w, int x, int y, int z) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return null;

        return null;// FIXME t.getPickedItem(target);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return super.getSelectedBoundingBoxFromPool(w, x, y, z);

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        MovingObjectPosition mop = rayTrace(w, x, y, z, RayTracer.getStartVector(player), RayTracer.getEndVector(player), t.getSelectionBoxes());
        if (mop != null) {
            return AABBUtils.translate((AxisAlignedBB) mop.hitInfo, x, y, z);
        }

        return super.getSelectedBoundingBoxFromPool(w, x, y, z);
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public int getRenderType() {

        return RenderMultipart.renderId;
    }

    public void setBlockBounds(AxisAlignedBB aabb) {

        setBlockBounds((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
    }

    private MovingObjectPosition rayTrace(World w, int x, int y, int z, Vector3 start, Vector3 end, List<AxisAlignedBB> aabbs) {

        List<MovingObjectPosition> mops = new ArrayList<MovingObjectPosition>();
        for (AxisAlignedBB aabb : aabbs) {
            setBlockBounds(aabb);
            MovingObjectPosition mop = super.collisionRayTrace(w, x, y, z, start.toVec3(), end.toVec3());
            if (mop != null) {
                mop.hitInfo = aabb;
                mops.add(mop);
            }
        }
        Collections.sort(mops, new ComparatorMOP(start));
        if (mops.isEmpty())
            return null;
        MovingObjectPosition mop = mops.get(0);
        setBlockBounds((AxisAlignedBB) mop.hitInfo);
        return mop;
    }
}
