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
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.util.ComparatorMOP;

import org.lwjgl.opengl.GL11;

public class BPTileMultipart extends TileEntity {
    
    private List<BPPart> parts = new ArrayList<BPPart>();
    
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
    
        return -1;// FIXME BLUEPOWER Get clicked part
    }
    
    public float getExplosionResistance() {
    
        float res = 0;
        
        for (BPPart p : parts)
            res = Math.max(res, p.getExplosionResistance());
        
        return res;
    }
    
    public ItemStack getPickedItem(MovingObjectPosition hit) {
    
        return null;// FIXME BLUEPOWER Get clicked part
    }
    
    public List<ItemStack> getDrops() {
    
        List<ItemStack> items = new ArrayList<ItemStack>();
        
        // FIXME BLUEPOWER Get clicked part
        
        return items;
    }
    
    public void notifyPartChange(BPPart part) {
    
        for (BPPart p : parts) {
            if (p == part) continue;
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
    
    public boolean onActivated(EntityPlayer player, MovingObjectPosition mop, ItemStack item) {
    
        return false;// FIXME BLUEPOWER Get clicked part
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
    
        super.writeToNBT(tag);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
    
        super.readFromNBT(tag);
    }
    
    public boolean drawHighlight(AxisAlignedBB cube, MovingObjectPosition mop) {
    
        return false;// FIXME BLUEPOWER Get clicked part
    }
    
    public List<BPPart> getParts() {
    
        return parts;
    }
    
}
