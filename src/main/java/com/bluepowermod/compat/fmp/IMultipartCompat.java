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

public interface IMultipartCompat {
    
    public BPPart getClickedPart(Vector3 loc, Vector3 subLoc, ItemStack item, EntityPlayer player);
    
    public int getInput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face);
    
    public void sendUpdatePacket(BPPart part);
    
    public boolean isMultipart(TileEntity te);
    
    public boolean checkOcclusion(TileEntity te, AxisAlignedBB box);
    
    public <T> T getBPPart(TileEntity te, Class<T> searchedClass);
    
    public <T> List<T> getBPParts(TileEntity te, Class<T> searchedClass);
    
    public int getMOPData(MovingObjectPosition mop);
    
    public static final class MultipartCompat {
        
        public static Class<? extends TileEntity> tile = null;
        
    }
    
}
