package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class BlockItemOre extends Block {
    private Item itemToDrop;
    public BlockItemOre(String type, Item item) {
        super(Material.iron);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
        this.setStepSound(soundTypeStone);
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.textureName = Refs.MODID + ":" + type;
        this.setBlockName(type);
        itemToDrop = item;
    }
    public Item getItemDropped(int par1, Random par2, int par3) {
        return this.itemToDrop;
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
