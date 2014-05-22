package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.init.Items;
import net.quetzi.bluepower.references.Refs;

public class BlockGemOre extends Block {
    public BlockGemOre(String type) {
        super(Material.iron);
        this.setCreativeTab(BluePower.creativeTab);
        this.setStepSound(soundTypeStone);
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.textureName = Refs.MODID + ":" + type;
    }
    public Item getItemDropped(int par1, Random par2, int par3) {
        Item dropped =  null;
        if (this.getUnlocalizedName().substring(5) == Refs.RUBYORE_NAME) {
            dropped = (Item) Items.ruby;
        }
        if (this.getUnlocalizedName().substring(5) == Refs.SAPPHIREORE_NAME) {
            dropped = (Item) Items.sapphire;
        }
        if (this.getUnlocalizedName().substring(5) == Refs.MALACHITEORE_NAME) {
            dropped = (Item) Items.malachite;
        }
        return dropped;
    }
    public int quantityDropped() {
        return 1;
    }
    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5));
    }
    public int getExpDrop(IBlockAccess world, Random x, int fortune) {
        return 1;
    }
}
