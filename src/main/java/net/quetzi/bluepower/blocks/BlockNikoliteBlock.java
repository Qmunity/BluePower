package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.items.ItemNikolite;
import net.quetzi.bluepower.references.Refs;

public class BlockNikoliteBlock extends Block {
    public BlockNikoliteBlock(String name) {
        super(Material.rock);
        this.textureName = Refs.MODID + ":" + name;
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
        this.setStepSound(soundTypeStone);
        this.setHardness(1.9F);
        this.setResistance(10.0F);
        this.setBlockName(name);
    }

    public Item getItemDropped(int par1, Random par2, int par3) {
        return new ItemNikolite();
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
