package net.quetzi.bluepower.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.quetzi.bluepower.blocks.BlockContainerBase;
import net.quetzi.bluepower.client.renderers.RenderLamp;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileLamp;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl)
 * 
 */
public class BlockLamp extends BlockContainerBase {
    
    private boolean isInverted;
    private String  colorName;
    private int     color;
    
    public BlockLamp(boolean _isInverted, String _colorName, int _color) {
    
        super(Material.iron);
        setInverted(_isInverted);
        colorName = _colorName;
        setColor(_color);
        setBlockName(Refs.LAMP_NAME + (isInverted ? "inverted" : "") + colorName);
        setCreativeTab(CustomTabs.tabBluePowerLighting);
        
    }
    
    @Override
    protected Class<? extends TileEntity> getTileEntity() {
    
        return TileLamp.class;
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.INVALID;
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
            return tileEntity.getLightLevel();
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
}
