package com.bluepowermod.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatModule;
import com.bluepowermod.tileentities.BPTileMultipart;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleFMPAlt extends CompatModule implements IMultipartCompat {
    
    @Override
    public void preInit(FMLPreInitializationEvent ev) {
    
        MultipartCompat.tile = BPTileMultipart.class;
    }
    
    @Override
    public void init(FMLInitializationEvent ev) {
    
    }
    
    @Override
    public void postInit(FMLPostInitializationEvent ev) {
    
    }
    
    @Override
    public void registerBlocks() {
    
    }
    
    @Override
    public void registerItems() {
    
    }
    
    @Override
    public void registerRenders() {
    
    }
    
    @Override
    public BPPart getClickedPart(Vector3 loc, Vector3 subLoc, EntityPlayer player, TileEntity tile) {
    
        return null;
    }
    
    @Override
    public int getInput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face) {
    
        return 0;
    }
    
    @Override
    public void sendUpdatePacket(BPPart part) {
    
        TileEntity te = part.getWorld().getTileEntity(part.getX(), part.getY(), part.getZ());
        if (isMultipart(te)) {
            ((BPTileMultipart) te).sendUpdatePacket();
        }
    }
    
    @Override
    public boolean isMultipart(TileEntity te) {
    
        return te instanceof BPTileMultipart;
    }
    
    @Override
    public boolean isOccupied(TileEntity tile, AxisAlignedBB box) {
    
        if (isMultipart(tile)) {
            BPTileMultipart multipart = (BPTileMultipart) tile;
            for (BPPart part : multipart.getParts()) {
                for (AxisAlignedBB occBox : part.getOcclusionBoxes()) {
                    if (occBox.intersectsWith(box)) return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public <T> T getBPPart(TileEntity te, Class<T> searchedClass) {
    
        List<T> l = getBPParts(te, searchedClass);
        if (l == null) return null;
        return l.size() > 0 ? l.get(0) : null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getBPParts(TileEntity te, Class<T> searchedClass) {
    
        if (!isMultipart(te)) return null;
        List<T> l = new ArrayList<T>();
        BPTileMultipart t = (BPTileMultipart) te;
        for (BPPart p : t.getParts()) {
            if (searchedClass.isAssignableFrom(p.getClass())) {
                l.add((T) p);
            }
        }
        return l;
    }
    
    @Override
    public int getMOPData(MovingObjectPosition mop) {
    
        return (Integer) mop.hitInfo;// TODO assign the subpart index hit to right clicking (for the Pneumatic Tube).
    }
    
}
