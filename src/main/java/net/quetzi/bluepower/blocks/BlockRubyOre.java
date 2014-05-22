package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.references.Refs;

public class BlockRubyOre extends Block {
    public BlockRubyOre() {
        super(Material.rock);
        this.textureName = Refs.MODID + ":" + Refs.RUBYORE_NAME;
        this.setCreativeTab(BluePower.creativeTab);
        this.setStepSound(soundTypeStone);
        this.setHardness(1.9F);
        this.setResistance(10.0F);
    }

    public Item getItemDropped(int par1, Random par2, int par3) {
        return Item.getItemFromBlock(Block.getBlockFromName(Refs.MODID + ":" + this.getUnlocalizedName().substring(5)));
    }
    public int quantityDropped() {
        return 1;
    }
    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon(Refs.MODID + ":" + Refs.RUBYORE_NAME);
    }
}
