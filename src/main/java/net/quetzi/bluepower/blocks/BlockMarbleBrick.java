package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.quetzi.bluepower.references.Refs;

public class BlockMarbleBrick extends Block {
    public BlockMarbleBrick() {
        super(Material.rock);
        this.textureName = Refs.MARBLEBRICK_TEXTURE_NAME;
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setStepSound(soundTypeStone);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
    }
    
    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon("bluepower:" + Refs.MARBLEBRICK_TEXTURE_NAME);
    }
}
