package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.references.Refs;

public class BlockMalachiteBlock extends Block {
    public BlockMalachiteBlock() {
        super(Material.rock);
        this.textureName = Refs.MODID + ":" + Refs.MALACHITEBLOCK_NAME;
        this.setCreativeTab(BluePower.creativeTab);
        this.setStepSound(soundTypeStone);
        this.setHardness(1.9F);
        this.setResistance(10.0F);
    }

    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon(Refs.MODID + ":" + Refs.MALACHITEBLOCK_NAME);
    }
}