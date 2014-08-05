package com.bluepowermod.blocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.client.renderers.RenderMultipart;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageMultipartRemove;
import com.bluepowermod.raytrace.BPMop;
import com.bluepowermod.raytrace.RayTracer;
import com.bluepowermod.tileentities.BPTileMultipart;
import com.bluepowermod.util.AABBUtils;
import com.bluepowermod.util.ComparatorMOP;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BPBlockMultipart extends BlockContainer {

    public BPBlockMultipart() {

        super(Material.rock);
        MinecraftForge.EVENT_BUS.register(this);
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
    public boolean canConnectRedstone(IBlockAccess w, int x, int y, int z, int side) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return false;

        return t.canConnectRedstone(ForgeDirection.getOrientation(side));
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {

        return isSideSolid(world, x, y, z, ForgeDirection.UP);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 start, Vec3 end) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            BPMop mop = rayTrace(w, x, y, z, RayTracer.getStartVector(player), RayTracer.getEndVector(player), t.getSelectionBoxes(), false);
            if (mop != null)
                setBlockBounds(mop.getCubeHit());
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
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {

        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null)
            return super.getSelectedBoundingBoxFromPool(w, x, y, z);

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        BPMop mop = rayTrace(w, x, y, z, RayTracer.getStartVector(player), RayTracer.getEndVector(player), t.getSelectionBoxes(), false);
        if (mop != null) {
            setBlockBounds(mop.getCubeHit());
            return AABBUtils.translate(mop.getCubeHit(), x, y, z);
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
    public boolean renderAsNormalBlock() {

        return false;
    }

    @Override
    public int getRenderType() {

        return RenderMultipart.renderId;
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {

        Vector3 v = new Vector3(event.x, event.y, event.z, event.world);
        if (v.getBlock() != this)
            return;
        BPTileMultipart te = (BPTileMultipart) v.getTileEntity();
        if (te == null)
            return;
        BPMop mop = rayTrace(event.world, event.x, event.y, event.z, RayTracer.getStartVector(event.entityPlayer),
                RayTracer.getEndVector(event.entityPlayer), te.getParts());

        System.out.println(FMLCommonHandler.instance().getEffectiveSide() + " - " + te.getParts().size());

        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            if (mop != null) {
                te.onActivated(event.entityPlayer, mop, event.entityPlayer.getItemInUse());
                System.out.println("Click!");
            } else {
                System.out.println("Ughh... no block here, sir");
            }
        }
    }

    public BPMop rayTrace(World w, int x, int y, int z, Vector3 start, Vector3 end, List<AxisAlignedBB> aabbs, boolean unused) {

        List<BPMop> mops = new ArrayList<BPMop>();
        for (AxisAlignedBB aabb : aabbs) {
            setBlockBounds(aabb);
            MovingObjectPosition mop = super.collisionRayTrace(w, x, y, z, start.toVec3(), end.toVec3());
            if (mop != null) {
                mops.add(new BPMop(mop, aabb));
            }
        }
        Collections.sort(mops, new ComparatorMOP(start));
        if (mops.isEmpty())
            return null;
        BPMop mop = mops.get(0);
        return mop;
    }

    public BPMop rayTrace(World w, int x, int y, int z, Vector3 start, Vector3 end, List<BPPart> parts) {

        List<BPMop> mops = new ArrayList<BPMop>();
        for (BPPart part : parts) {
            for (AxisAlignedBB aabb : part.getSelectionBoxes()) {
                setBlockBounds(aabb);
                MovingObjectPosition mop = super.collisionRayTrace(w, x, y, z, start.toVec3(), end.toVec3());
                if (mop != null) {
                    mops.add(new BPMop(mop, part, aabb));
                }
            }
        }
        Collections.sort(mops, new ComparatorMOP(start));
        if (mops.isEmpty())
            return null;
        BPMop mop = mops.get(0);
        return mop;
    }

    public void setBlockBounds(AxisAlignedBB aabb) {

        setBlockBounds((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
    }

    @Override
    public boolean isBlockSolid(IBlockAccess w, int x, int y, int z, int side) {

        return isSideSolid(w, x, y, z, ForgeDirection.getOrientation(side));
    }

    @Override
    public boolean isSideSolid(IBlockAccess w, int x, int y, int z, ForgeDirection side) {

        BPTileMultipart te = getTile(w, x, y, z);
        if (te == null)
            return false;

        return te.isFaceSolid(side);
    }

    @Override
    public void onNeighborBlockChange(World w, int x, int y, int z, Block b) {

        BPTileMultipart te = getTile(w, x, y, z);
        if (te == null)
            return;
        te.onNeighborUpdate();
    }

    @Override
    public void onNeighborChange(IBlockAccess w, int x, int y, int z, int tileX, int tileY, int tileZ) {

        BPTileMultipart te = getTile(w, x, y, z);
        if (te == null)
            return;
        te.onNeighborUpdate();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(MovingObjectPosition target, World w, int x, int y, int z) {

        BPTileMultipart te = getTile(w, x, y, z);
        if (te == null)
            return null;
        BPMop mop = rayTrace(w, x, y, z, RayTracer.getStartVector(Minecraft.getMinecraft().thePlayer),
                RayTracer.getEndVector(Minecraft.getMinecraft().thePlayer), te.getParts());
        if (mop == null)
            return null;
        return mop.getPartHit().getPickedItem(mop);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {

        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {

        return false;
    }

    // Breaking...

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onBlockDestroyedByPlayer(World w, int x, int y, int z, int meta) {

        BPTileMultipart te = getTile(w, x, y, z);
        if (te == null)
            return;
        BPMop mop = rayTrace(w, x, y, z, RayTracer.getStartVector(Minecraft.getMinecraft().thePlayer),
                RayTracer.getEndVector(Minecraft.getMinecraft().thePlayer), te.getParts());
        if (mop == null)
            return;
        NetworkHandler.sendToServer(new MessageMultipartRemove(te.getPartId(mop.getPartHit()), w));
    }

    @Override
    public boolean removedByPlayer(World w, EntityPlayer player, int x, int y, int z, boolean willHarvest) {

        BPTileMultipart te = getTile(w, x, y, z);
        if (te == null)
            return false;

        BPMop mop = rayTrace(w, x, y, z, RayTracer.getStartVector(Minecraft.getMinecraft().thePlayer),
                RayTracer.getEndVector(Minecraft.getMinecraft().thePlayer), te.getParts());
        if (mop == null)
            return false;

        if (w.isRemote) {
            w.setBlock(x, y, z, this);
            w.setTileEntity(x, y, z, te);
            te.removePart(mop.getPartHit());
            return false;
        }

        te.removePart(mop.getPartHit());

        if (te.getParts().size() == 0)
            return true;

        return false;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {

        return true;
    }
}
