package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.quetzi.bluepower.references.Refs;

public class BlockBasaltCobble extends Block {
    public BlockBasaltCobble() {
        super(Material.rock);
        this.textureName = Refs.BASALTCOBBLE_TEXTURE_NAME;
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setStepSound(soundTypeStone);
    }
    
    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon("bluepower:" + Refs.BASALTCOBBLE_TEXTURE_NAME);
    }
}
