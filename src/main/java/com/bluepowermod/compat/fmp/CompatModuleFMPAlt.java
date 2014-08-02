package com.bluepowermod.compat.fmp;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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
    public BPPart getClickedPart(Vector3 loc, Vector3 subLoc, ItemStack item, EntityPlayer player) {
    
        return null;
    }
    
    @Override
    public int getInput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face) {
    
        return 0;
    }
    
    @Override
    public void sendUpdatePacket(BPPart part) {
    
    }
    
    @Override
    public boolean isMultipart(TileEntity te) {
    
        return false;
    }
    
    @Override
    public boolean checkOcclusion(TileEntity tile, AxisAlignedBB box) {
    
        return false;
    }
    
    @Override
    public <T> T getBPPart(TileEntity te, Class<T> searchedClass) {
    
        return null;
    }
    
    @Override
    public <T> List<T> getBPParts(TileEntity te, Class<T> searchedClass) {
    
        return null;
    }
    
    @Override
    public int getMOPData(MovingObjectPosition mop) {
    
        return (Integer) mop.hitInfo;//TODO assign the subpart index hit to right clicking (for the Pneumatic Tube).
    }
    
}
