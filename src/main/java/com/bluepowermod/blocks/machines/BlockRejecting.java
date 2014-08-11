package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;
import com.bluepowermod.tileentities.IRejectAnimator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRejecting extends BlockContainerBase {
    
    public BlockRejecting(Material material, Class<? extends TileEntity> tileEntityClass) {
    
        super(material, tileEntityClass);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        super.registerBlockIcons(iconRegister);
        for (String name : textures.keySet()) {
            if (name.contains("side")) textures.put(name + "_rejecting", iconRegister.registerIcon(name + "_rejecting"));
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getIcon(EnumFaceType faceType, boolean ejecting, boolean powered, int side, TileEntity te) {
    
        boolean isRejecting = ((IRejectAnimator) te).isRejecting();
        if (faceType == EnumFaceType.SIDE && isRejecting) {
            String iconName = getIconName(faceType, ejecting, powered);
            return textures.get(iconName + "_rejecting");
        } else {
            return super.getIcon(faceType, isRejecting, powered, side, te);
        }
    }
}
