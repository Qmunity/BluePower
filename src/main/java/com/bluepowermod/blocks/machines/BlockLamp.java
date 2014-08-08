package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RenderLamp;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.tileentities.tier1.TileLamp;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl)
 * 
 */
public class BlockLamp extends BlockContainerBase {
    
    private boolean      isInverted;
    private final String colorName;
    private int          color;
    
    public BlockLamp(boolean _isInverted, String _colorName, int _color) {
    
        super(Material.iron, TileLamp.class);
        setInverted(_isInverted);
        colorName = _colorName;
        setColor(_color);
        setBlockName(Refs.LAMP_NAME + (isInverted ? "inverted" : "") + colorName);
        setCreativeTab(CustomTabs.tabBluePowerLighting);
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        blockIcon = iconRegister.registerIcon(Refs.MODID + ":lamps/lamp_on");
        blockIcon = iconRegister.registerIcon(Refs.MODID + ":lamps/lamp_off");
    }
    
    @Override
    public int getLightValue(IBlockAccess w, int x, int y, int z) {
    
        TileLamp tileEntity = (TileLamp) w.getTileEntity(x, y, z);
        if (tileEntity != null) {
            int power = tileEntity.getPower();
            
            if (isInverted()) {
                power = 15 - power;
            }
            return power;
        } else {
            return 0;
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
    
        return RenderLamp.RENDER_ID;
    }
    
    public int getColor() {
    
        return color;
    }
    
    public void setColor(int color) {
    
        this.color = color;
    }
    
    public boolean isInverted() {
    
        return isInverted;
    }
    
    public void setInverted(boolean isInverted) {
    
        this.isInverted = isInverted;
    }

    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side){

        return true;
    }

    @Override
    public boolean canRenderInPass(int pass){
        return true;
    }

    @Override
    public int getRenderBlockPass(){
        return 1;
    }
}
