package com.bluepowermod.tileentities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.api.part.IBPFacePart;
import com.bluepowermod.api.part.redstone.IBPRedstonePart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.blocks.BPBlockMultipart;
import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.raytrace.BPMop;
import com.bluepowermod.raytrace.RayTracer;
import com.bluepowermod.util.ComparatorMOP;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BPTileMultipart extends TileEntity {
    
    private final List<BPPart>      parts          = new ArrayList<BPPart>();
    private final Map<BPPart, UUID> partIds        = new HashMap<BPPart, UUID>();
    private boolean                 shouldReRender = false;
    
    public List<AxisAlignedBB> getCollisionBoxes() {
    
        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        for (BPPart p : parts)
            aabbs.addAll(p.getCollisionBoxes());
        return aabbs;
    }
    
    public List<AxisAlignedBB> getSelectionBoxes() {
    
        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        for (BPPart p : parts)
            aabbs.addAll(p.getSelectionBoxes());
        return aabbs;
    }
    
    public List<AxisAlignedBB> getOcclusionBoxes() {
    
        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        for (BPPart p : parts)
            aabbs.addAll(p.getOcclusionBoxes());
        return aabbs;
    }
    
    public MovingObjectPosition rayTrace(Vector3 start, Vector3 end) {
    
        List<MovingObjectPosition> mops = new ArrayList<MovingObjectPosition>();
        
        for (BPPart p : parts) {
            MovingObjectPosition mop = p.rayTrace(start, end);
            if (mop != null) mops.add(mop);
        }
        
        Collections.sort(mops, new ComparatorMOP(start));
        
        if (mops.isEmpty()) return null;
        
        return mops.get(0);
    }
    
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 loc, int pass, float frame) {
    
        for (BPPart p : parts) {
            GL11.glPushMatrix();
            p.renderDynamic(loc, pass, frame);
            GL11.glPopMatrix();
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void renderStatic(Vector3 loc, int pass) {
    
        for (BPPart p : parts) {
            GL11.glPushMatrix();
            p.renderStatic(loc, pass);
            GL11.glPopMatrix();
        }
    }
    
    public int getLightValue() {
    
        int val = 0;
        
        for (BPPart p : parts)
            val = Math.max(val, p.getLightValue());
        
        return Math.max(0, Math.min(val, 15));
    }
    
    public float getHardness(MovingObjectPosition mop, EntityPlayer player) {
    
        BPMop bpmop = BPBlockMultipart.rayTrace(worldObj, xCoord, yCoord, zCoord, RayTracer.getStartVector(player), RayTracer.getEndVector(player), getParts());
        if (bpmop != null) return bpmop.getPartHit().getHardness(mop, player);
        return -1;
    }
    
    public float getExplosionResistance() {
    
        float res = 0;
        
        for (BPPart p : parts)
            res = Math.max(res, p.getExplosionResistance());
        
        return res;
    }
    
    public List<ItemStack> getDrops() {
    
        List<ItemStack> items = new ArrayList<ItemStack>();
        
        return items;
    }
    
    public void notifyPartChange(BPPart part) {
    
        for (BPPart p : parts) {
            if (p == part) continue;
            p.onPartChanged();
        }
        shouldReRender = true;
    }
    
    public void onEntityCollision(Entity entity) {
    
        for (BPPart p : parts) {
            p.setWorld(worldObj);
            p.setX(xCoord);
            p.setY(yCoord);
            p.setZ(zCoord);
            p.onEntityCollision(entity);// FIXME BLUEPOWER Check if entity is actually colliding
        }
    }
    
    public void onNeighborUpdate() {
    
        for (BPPart p : parts) {
            p.setWorld(worldObj);
            p.setX(xCoord);
            p.setY(yCoord);
            p.setZ(zCoord);
            p.onNeighborUpdate();
        }
        
        shouldReRender = true;
    }
    
    public boolean canConnectRedstone(ForgeDirection side) {
    
        for (BPPart p : parts) {
            p.setWorld(worldObj);
            p.setX(xCoord);
            p.setY(yCoord);
            p.setZ(zCoord);
            if (p instanceof IBPRedstonePart) {
                ForgeDirection s = side;
                if (p instanceof IBPFacePart) s = s.getRotation(ForgeDirection.getOrientation(((IBPFacePart) p).getFace()));
                if (((IBPRedstonePart) p).canConnect(s)) return true;
            }
        }
        
        return false;
    }
    
    public boolean onActivated(EntityPlayer player, BPMop mop, ItemStack item) {
    
        return mop.getPartHit().onActivated(player, mop, item);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
    
        super.writeToNBT(tag);
        
        int i = 0;
        for (BPPart p : parts) {
            NBTTagCompound t = new NBTTagCompound();
            p.save(t);
            tag.setString("partType" + i, p.getType());
            tag.setTag("part" + i, t);
            i++;
        }
        tag.setInteger("partAmount", i);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
    
        super.readFromNBT(tag);
        int amt = tag.getInteger("partAmount");
        for (int i = 0; i < amt; i++) {
            String type = tag.getString("partType" + i);
            NBTTagCompound t = tag.getCompoundTag("part" + i);
            BPPart p = PartRegistry.getInstance().createPart(type);
            p.load(t);
            addPart(p);
        }
    }
    
    private long ticks = 0;
    
    @Override
    public void updateEntity() {
    
        if (parts.size() == 0 && !worldObj.isRemote) {
            worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
            worldObj.setTileEntity(xCoord, yCoord, zCoord, null);
            return;
        }
        
        List<BPPart> removed = new ArrayList<BPPart>();
        for (BPPart p : parts) {
            p.setWorld(worldObj);
            p.setX(xCoord);
            p.setY(yCoord);
            p.setZ(zCoord);
            if (p instanceof BPPartFace) if (!((BPPartFace) p).canStay()) removed.add(p);
        }
        for (BPPart p : removed)
            removePart(p);
        removed.clear();
        
        for (BPPart p : parts)
            p.update();
        
        if (ticks == 5 || ticks % 200 == 0) {
            shouldReRender = true;
        }
        ticks++;
    }
    
    public List<BPPart> getParts() {
    
        return parts;
    }
    
    private void addPart(BPPart part, UUID id) {
    
        parts.add(part);
        partIds.put(part, id);
        shouldReRender = true;
        for (BPPart p : parts)
            if (p != part) p.onPartChanged();
        sendUpdatePacket();
    }
    
    public void addPart(BPPart part) {
    
        addPart(part, UUID.randomUUID());
    }
    
    public void removePart(BPPart part) {
    
        parts.remove(part);
        partIds.remove(part);
        shouldReRender = true;
        for (BPPart p : parts)
            if (p != part) p.onPartChanged();
        sendUpdatePacket();
    }
    
    public boolean isFaceSolid(ForgeDirection orientation) {
    
        for (BPPart p : parts)
            if (p.isFaceSolid(orientation)) return true;
        
        return false;
    }
    
    public void sendUpdatePacket() {
    
        if (worldObj == null) return;
        if (worldObj.isRemote) return;
        
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public Packet getDescriptionPacket() {
    
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, getUpdatePacketData());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    
        readUpdatePacketData(pkt.func_148857_g());
    }
    
    public NBTTagCompound getUpdatePacketData() {
    
        NBTTagCompound tag = new NBTTagCompound();
        
        int i = 0;
        for (BPPart p : parts) {
            NBTTagCompound t = new NBTTagCompound();
            p.save(t);
            tag.setString("partType" + i, p.getType());
            tag.setString("partId" + i, partIds.get(p).toString());
            tag.setTag("part" + i, t);
            i++;
        }
        tag.setInteger("partAmount", i);
        
        return tag;
    }
    
    public void readUpdatePacketData(NBTTagCompound tag) {
    
        int amt = tag.getInteger("partAmount");
        List<BPPart> found = new ArrayList<BPPart>();
        for (int i = 0; i < amt; i++) {
            String type = tag.getString("partType" + i);
            UUID id = UUID.fromString(tag.getString("partId" + i));
            NBTTagCompound t = tag.getCompoundTag("part" + i);
            BPPart p = getPartForId(id);
            if (p == null) addPart(p = PartRegistry.getInstance().createPart(type), id);
            p.load(t);
            found.add(p);
        }
        List<BPPart> removed = new ArrayList<BPPart>();
        for (BPPart p : getParts()) {
            if (!found.contains(p)) {
                removed.add(p);
            }
        }
        for (BPPart p : removed)
            removePart(p);
        
        found.clear();
        removed.clear();
        
        shouldReRender = true;
    }
    
    private BPPart getPartForId(UUID id) {
    
        for (Entry<BPPart, UUID> e : partIds.entrySet())
            if (e.getValue().equals(id)) return e.getKey();
        
        return null;
    }
    
    public void onClientBreakPart(UUID id) {
    
        BPPart p = getPartForId(id);
        if (p != null) removePart(p);
        sendUpdatePacket();
    }
    
    public UUID getPartId(BPPart part) {
    
        try {
            return partIds.get(part);
        } catch (Exception ex) {
        }
        return null;
    }
    
    public boolean shouldReRender() {
    
        boolean should = shouldReRender;
        for (BPPart p : getParts()) {
            if (p.shouldReRender()) {
                should = true;
                p.resetRenderUpdate();
            }
        }
        shouldReRender = false;
        return should;
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
    
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }
    
}
