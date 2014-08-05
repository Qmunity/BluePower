package com.bluepowermod.blocks.computer;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.Refs;
import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier3.TileIOExpander;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockIOExpander extends BlockContainerBase {
    
    @SideOnly(Side.CLIENT)
    protected IIcon topTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon frontTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon sideTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon backTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon bottomTexture;
    
    public BlockIOExpander() {
    
        super(Material.iron, TileIOExpander.class);
        setBlockName(Refs.BLOCKIOEXPANDER_NAME);
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.IO_EXPANDER;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
    
        ForgeDirection dir = ForgeDirection.getOrientation(meta);
        if (side == dir.ordinal()) {
            return topTexture;
        } else if (side == dir.getOpposite().ordinal()) {
            return bottomTexture;
        } else if (side == dir.WEST.ordinal()) {
            return frontTexture;
        } else if (side == dir.EAST.ordinal()) { return backTexture; }
        return sideTexture;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    
        TileIOExpander tile = (TileIOExpander) world.getTileEntity(x, y, z);
        ForgeDirection dir = tile.getFacingDirection();
        
        if (dir.ordinal() == side) {
            return frontTexture;
        } else if (dir.getOpposite().ordinal() == side) {
            return backTexture;
        } else if (dir.UP.ordinal() == side) {
            return topTexture;
        } else if (dir.DOWN.ordinal() == side) {
            return bottomTexture;
        } else {
            return sideTexture;
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        frontTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "io_expander_front");
        sideTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_side");
        topTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_top");
        backTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "io_expander_back");
        bottomTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_bottom");
    }
    
}
