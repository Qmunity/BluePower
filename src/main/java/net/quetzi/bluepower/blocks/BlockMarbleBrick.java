package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.references.Refs;

public class BlockMarbleBrick extends Block {
    public BlockMarbleBrick() {
        super(Material.rock);
        this.textureName = Refs.MODID + ":" + Refs.MARBLEBRICK_NAME;
        this.setCreativeTab(BluePower.creativeTab);
        this.setStepSound(soundTypeStone);
        this.setHardness(1.9F);
        this.setResistance(10.0F);
    }
    
    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon(Refs.MODID + ":" + Refs.MARBLEBRICK_NAME);
    }
}
