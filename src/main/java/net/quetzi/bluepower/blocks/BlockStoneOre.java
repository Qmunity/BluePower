package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.references.Refs;

public class BlockStoneOre extends Block {

    public BlockStoneOre(String name) {
        super(Material.rock);
        this.setBlockName(name);
        this.setHardness(1.9F);
        if (name == Refs.BASALT_NAME) {
            this.setResistance(25.0F);
        } else {
            this.setResistance(10.0F);
        }
        this.textureName = Refs.MODID + ":" + name;
        this.setCreativeTab(BluePower.creativeTab);
        this.setStepSound(soundTypeStone);
    }

    public Item getItemDropped(int par1, Random par2, int par3) {
        if (this.getUnlocalizedName().substring(5) == Refs.BASALT_NAME) {
            return Item.getItemFromBlock(Block.getBlockFromName(Refs.MODID + ":"
                    + Refs.BASALTCOBBLE_NAME));
        }
        return Item.getItemFromBlock(Block.getBlockFromName(Refs.MODID + ":"
                + this.getUnlocalizedName().substring(5)));
    }

    public int quantityDropped() {
        return 1;
    }

    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5));
    }
}
