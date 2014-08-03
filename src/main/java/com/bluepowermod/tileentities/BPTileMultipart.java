package com.bluepowermod.tileentities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.api.part.PartRegistry;
import com.bluepowermod.api.part.redstone.IBPRedstonePart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageMultipartUpdate;
import com.bluepowermod.raytrace.BPMop;
import com.bluepowermod.raytrace.RayTracer;
import com.bluepowermod.util.ComparatorMOP;

public class BPTileMultipart extends TileEntity {

    private List<BPPart> parts = new ArrayList<BPPart>();
    private boolean shouldReRender = false;

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
            if (mop != null)
                mops.add(mop);
        }

        Collections.sort(mops, new ComparatorMOP(start));

        if (mops.isEmpty())
            return null;

        return mops.get(0);
    }

    public void renderDynamic(Vector3 loc, int pass, float frame) {

        for (BPPart p : parts) {
            GL11.glPushMatrix();
            p.renderDynamic(loc, pass, frame);
            GL11.glPopMatrix();
        }
    }

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

        BPPart p = RayTracer.getSelectedPart(mop, player);
        if (p != null)
            return p.getHardness(mop, player);
        return -1;
    }

    public float getExplosionResistance() {

        float res = 0;

        for (BPPart p : parts)
            res = Math.max(res, p.getExplosionResistance());

        return res;
    }

    public ItemStack getPickedItem(MovingObjectPosition mop, EntityPlayer player) {

        BPPart p = RayTracer.getSelectedPart(mop, player);
        if (p != null)
            return p.getPickedItem(mop);
        return null;
    }

    public List<ItemStack> getDrops() {

        List<ItemStack> items = new ArrayList<ItemStack>();

        return items;
    }

    public void notifyPartChange(BPPart part) {

        for (BPPart p : parts) {
            if (p == part)
                continue;
            p.onPartChanged();
        }
    }

    public void onEntityCollision(Entity entity) {

        for (BPPart p : parts) {
            p.onEntityCollision(entity);// FIXME BLUEPOWER Check if entity is actually colliding
        }
    }

    public void onNeighborUpdate() {

        for (BPPart p : parts)
            p.onNeighborUpdate();
    }

    public boolean canConnectRedstone(ForgeDirection side) {

        for (BPPart p : parts)
            if (p instanceof IBPRedstonePart)
                if (((IBPRedstonePart) p).canConnect(side))
                    return true;

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
            BPPart p = PartRegistry.createPart(type);
            p.load(t);
        }
    }

    public boolean drawHighlight(AxisAlignedBB cube, MovingObjectPosition mop, EntityPlayer player) {

        BPPart p = RayTracer.getSelectedPart(mop, player);
        if (p != null)
            return p.drawHighlight(cube, mop);
        return false;
    }

    @Override
    public void updateEntity() {

        List<BPPart> removed = new ArrayList<BPPart>();
        for (BPPart p : parts) {
            p.setWorld(worldObj);
            p.setX(xCoord);
            p.setY(yCoord);
            p.setZ(zCoord);
            if (p instanceof BPPartFace)
                if (!((BPPartFace) p).canStay())
                    removed.add(p);
        }
        for (BPPart p : removed)
            removePart(p);
        removed.clear();

        for (BPPart p : parts)
            p.update();

        if (shouldReRender) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            shouldReRender = false;
        }
    }

    public List<BPPart> getParts() {

        return parts;
    }

    public void addPart(BPPart part) {

        parts.add(part);
        shouldReRender = true;
        for (BPPart p : parts)
            if (p != part)
                p.onPartChanged();
        sendUpdatePacket();
    }

    public void removePart(BPPart part) {

        parts.remove(part);
        shouldReRender = true;
        for (BPPart p : parts)
            if (p != part)
                p.onPartChanged();
        sendUpdatePacket();
    }

    public boolean isFaceSolid(ForgeDirection orientation) {

        for (BPPart p : parts)
            if (p.isFaceSolid(orientation))
                return true;

        return false;
    }

    public void sendUpdatePacket() {

        if (worldObj == null)
            return;
        if (worldObj.isRemote)
            return;
        NetworkHandler.sendToAllAround(new MessageMultipartUpdate(getUpdatePacketData()), worldObj);
    }

    public NBTTagCompound getUpdatePacketData() {

        NBTTagCompound tag = new NBTTagCompound();

        int i = 0;
        for (BPPart p : parts) {
            NBTTagCompound t = new NBTTagCompound();
            p.save(t);
            tag.setString("partType" + i, p.getType());
            tag.setTag("part" + i, t);
            i++;
        }
        tag.setInteger("partAmount", i);

        return tag;
    }

    public void readUpdatePacketData(NBTTagCompound tag) {

        int amt = tag.getInteger("partAmount");
        for (int i = 0; i < amt; i++) {
            String type = tag.getString("partType" + i);
            NBTTagCompound t = tag.getCompoundTag("part" + i);
            BPPart p = PartRegistry.createPart(type);
            p.load(t);
        }
    }

}
