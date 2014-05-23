package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.items.ItemMalachite;
import net.quetzi.bluepower.items.ItemRuby;
import net.quetzi.bluepower.items.ItemSapphire;
import net.quetzi.bluepower.references.Refs;

public class BlockGemOre extends Block {
    public BlockGemOre(String type) {
        super(Material.iron);
        this.setCreativeTab(BluePower.creativeTab);
        this.setStepSound(soundTypeStone);
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.textureName = Refs.MODID + ":" + type;
        this.setBlockName(type);
    }
    public Item getItemDropped(int par1, Random par2, int par3) {
        Item dropped =  null;
        if (this.getUnlocalizedName().substring(5).matches(Refs.RUBYORE_NAME)) {
            return new ItemRuby();
        }
        if (this.getUnlocalizedName().substring(5).matches(Refs.SAPPHIREORE_NAME)) {
            return new ItemSapphire();
        }
        if (this.getUnlocalizedName().substring(5).matches(Refs.MALACHITEORE_NAME)) {
            return new ItemMalachite();
        }
        return dropped;
    }
    public int quantityDropped() {
        int quantity = new Random().nextInt(2);
        return quantity + 2;
    }
    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5));
    }
    public int getExpDrop(IBlockAccess world, Random x, int fortune) {
        return 1;
    }
    protected boolean canSilkHarvest() {
        return true;
    }
}
