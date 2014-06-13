package net.quetzi.bluepower.compat.fmp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.vec.Vector3;

public interface IMultipartCompat {
    
    public BPPart getClickedPart(Vector3 loc, Vector3 subLoc, MovingObjectPosition mop, ItemStack item, EntityPlayer player);
    
}
