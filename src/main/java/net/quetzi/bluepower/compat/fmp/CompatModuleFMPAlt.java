package net.quetzi.bluepower.compat.fmp;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.compat.CompatModule;


public class CompatModuleFMPAlt extends CompatModule implements IMultipartCompat {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {
        
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
    public BPPart getClickedPart(Vector3 loc, Vector3 subLoc, MovingObjectPosition mop, ItemStack item, EntityPlayer player) {
    
        return null;
    }
    
}
